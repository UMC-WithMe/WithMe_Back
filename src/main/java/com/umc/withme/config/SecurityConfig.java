package com.umc.withme.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.withme.dto.common.ErrorResponse;
import com.umc.withme.exception.ExceptionType;
import com.umc.withme.security.JwtAuthenticationFilter;
import com.umc.withme.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String BASE_URL = "/api";
    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/actuator/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                // JWT 기반 인증이기 때문에 session 사용 x
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .antMatchers(AUTH_WHITELIST).permitAll()
                        .mvcMatchers(BASE_URL + "/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    // 권한 문제가 발생했을 때 이 부분을 호출한다.
                    log.error("SecurityConfig.SecurityFilterChain.accessDeniedHandler() ex={}", String.valueOf(accessDeniedException));

                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("application/json; charset=UTF-8");

                    int errorCode = ExceptionType.ACCESS_DENIED_EXCEPTION.getErrorCode();
                    String errorMessage = ExceptionType.ACCESS_DENIED_EXCEPTION.getErrorMessage();
                    ErrorResponse errorResponse = new ErrorResponse(errorCode, errorMessage);

                    response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    // 인증문제가 발생했을 때 이 부분을 호출한다.
                    log.error("SecurityConfig.SecurityFilterChain.authenticationEntryPoint() ex={}", String.valueOf(authException));

                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("application/json; charset=UTF-8");

                    int errorCode = ExceptionType.AUTHENTICATION_EXCEPTION.getErrorCode();
                    String errorMessage = ExceptionType.AUTHENTICATION_EXCEPTION.getErrorMessage();
                    ErrorResponse errorResponse = new ErrorResponse(errorCode, errorMessage);

                    response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
                }).and()
                .build();
    }
}
