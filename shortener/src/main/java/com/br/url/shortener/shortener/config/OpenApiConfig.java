package com.br.url.shortener.shortener.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("URL Shortener API")
                        .description("API REST para encurtamento de URLs com contagem de cliques e expiração automática.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Seu Nome")
                                .url("https://github.com/seuusuario")
                        )
                );
    }
}
