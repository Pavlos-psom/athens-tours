package com.athenstours.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger UI (springdoc) configuration. Adds the "Authorize" button with a JWT bearer
 * field, so a token obtained from POST /api/v1/auth/authenticate can be pasted once and
 * reused for every protected endpoint tried from the UI - without this bean, Swagger UI
 * still lists every endpoint (springdoc auto-generates that from the controllers), but
 * there is no way to attach an Authorization header to a "Try it out" call.
 */
@Configuration
public class OpenApiConfig {

    private static final String BEARER_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI athensToursOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Athens Tours API")
                        .description("REST API for browsing tours, tour guides and schedules, "
                                + "and booking seats on them.")
                        .version("v1"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME_NAME, new SecurityScheme()
                                .name(BEARER_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
