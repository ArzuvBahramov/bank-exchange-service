package com.exchange.bank.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
public class OpenApiConfig {
    final static String SECURITY_SCHEME_NAME = "bearerAuth";
    final static String SECURITY_SCHEME = "bearer";
    final static String SECURITY_BEARER_FORMAT = "JWT";

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = SECURITY_SCHEME_NAME;
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme(SECURITY_SCHEME)
                                        .bearerFormat(SECURITY_BEARER_FORMAT)
                        )
                );
    }
}
