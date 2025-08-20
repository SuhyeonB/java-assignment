package com.example.assignment.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Assignment API")
                .version("v1.0")
                .description("Spring Security + JWT 과제 API")
        ).components(new Components().addSecuritySchemes(
                "bearerAuth",
                new SecurityScheme()
                        .name("bearerAuth").type(SecurityScheme.Type.HTTP)
                        .scheme("bearer").bearerFormat("JWT")
        ));
    }
}
