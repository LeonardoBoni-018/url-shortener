package com.br.url.shortener.shortener.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.URL;

public record CreateUrlRequest(

        @NotBlank(message = "A URL original é obrigatória")
        @URL(message = "A URL informada não é válida")
        String originalUrl,

        @Positive(message = "O prazo de expiração deve ser positivo")
        Integer expiresInDays
) {}