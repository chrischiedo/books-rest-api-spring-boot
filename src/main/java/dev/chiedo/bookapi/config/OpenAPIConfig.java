package dev.chiedo.bookapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        Contact contact = new Contact();
        contact.setName("Chris Chiedo");
        contact.setEmail("chiedo@example.com");
        contact.setUrl("https://chrischiedo.github.io/");

        License license = new License().name("MIT License");

        Info info = new Info()
                .title("Books API Documentation")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints for managing books.")
                .license(license)
                .summary("A minimal book REST API documentation");

        return new OpenAPI()
                .info(info);
    }
}

