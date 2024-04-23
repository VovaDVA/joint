package com.jointAuth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String bearerSchemeName = "bearer-key";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(bearerSchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        bearerSchemeName,
                                        new SecurityScheme()
                                                .name(bearerSchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}
