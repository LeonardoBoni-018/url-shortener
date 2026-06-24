package com.br.url.shortener.shortener.dto.response;
import java.time.LocalDateTime;
import java.util.UUID;

public record UrlResponse(
        UUID id,
        String shortCode,
        String shortUrl,
        String originalUrl,
        Long clickCount,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) {}

