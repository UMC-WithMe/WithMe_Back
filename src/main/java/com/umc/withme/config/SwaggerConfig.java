package com.umc.withme.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // TODO: 추후에 Spring Security, @ControllerAdvice, Actuator 등을 고려한 설정을 추가해야 함
    @Bean
    public OpenAPI withMeApi(@Value("withme.application.version") String appVersion) {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("WithMe API")
                                .description("WithMe API 명세서")
                                .version(appVersion)
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Team WithMe GitHub Organization")
                                .url("https://github.com/UMC-WithMe")
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "access-token",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}
