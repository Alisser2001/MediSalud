package com.alidev.medisalud.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI mediSaludOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("MediSalud API")
                                .description("Medical appointment scheduling API built with Spring Boot and Hexagonal Architecture.")
                                .version("1.0.0")
                                .contact(new Contact().name("Alidev"))
                )
                .servers(
                        List.of(
                                new Server()
                                        .url("http://localhost:8080")
                                        .description("Local Environment")
                        )
                );
    }
}