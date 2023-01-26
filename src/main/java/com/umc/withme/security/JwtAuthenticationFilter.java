package com.umc.withme.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.withme.dto.common.ErrorResponse;
import com.umc.withme.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 모든 요청마다 작동하여, jwt token을 확인한다.
     * 유효한 token이 있는 경우 token을 parsing해서 사용자 정보를 읽고 SecurityContext에 사용자 정보를 저장한다.
     *
     * @param request   HttpServletRequest 객체
     * @param response  HttpServletResponse 객체
     * @param filterChain   FilterChain 객체
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // Header에서 JWT 받아옴
        String authorization = jwtTokenProvider.resolveToken(request);

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);

            try {
                // Token의 유효성 검증
                jwtTokenProvider.validateToken(token);

                // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옴
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                // SecurityContext에 authentication 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                log.error("JwtAuthenticationFilter.doFilterInternal() ex={}", String.valueOf(e));
                setErrorResponse(e.getClass(), response);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Exception 정보를 입력받아 응답할 error response를 설정한다.
     *
     * @param classType Exception의 class type
     * @param response  HttpServletResponse 객체
     */
    private void setErrorResponse(
            Class<? extends Exception> classType,
            HttpServletResponse response
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");

        ExceptionType exceptionType = ExceptionType.fromByAuthenticationFailed(classType);
        ErrorResponse errorResponse = new ErrorResponse(
                exceptionType.getErrorCode(),
                exceptionType.getErrorMessage()
        );
        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }
}
