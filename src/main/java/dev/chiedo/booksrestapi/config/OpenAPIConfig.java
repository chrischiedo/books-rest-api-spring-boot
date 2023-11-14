package dev.chiedo.booksrestapi.config;

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
        contact.setEmail("chiedo@gmail.com");
        contact.setUrl("https://www.chiedo.dev");

        License license = new License().name("MIT License");

        Info info = new Info()
                .title("Books API Documentation")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints for managing books.")
                .license(license)
                .summary("A simple books API documentation");

        return new OpenAPI()
                .info(info);
    }
}
