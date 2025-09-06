package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI quoteApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Random Quote API")
                        .description("A RESTful API that provides random inspirational quotes with IP-based rate limiting to prevent abuse. " +
                                    "Each IP address is limited to 5 requests per minute.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Quote API Team")
                                .email("support@quoteapi.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
