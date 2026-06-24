package com.br.url.shortener.shortener.service;

import com.br.url.shortener.shortener.database.entity.ShortUrl;
import com.br.url.shortener.shortener.database.repository.ShortUrlRepository;
import com.br.url.shortener.shortener.dto.request.CreateUrlRequest;
import com.br.url.shortener.shortener.dto.response.UrlResponse;
import com.br.url.shortener.shortener.exception.custom.LinkExpiredException;
import com.br.url.shortener.shortener.exception.custom.ShortUrlNotFoundException;
import com.br.url.shortener.shortener.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final ShortCodeGenerator codeGenerator;

    @Value("${app.base-url}")
    private String baseUrl;

    @Transactional
    public UrlResponse create(CreateUrlRequest request) {
        String shortCode = generateUniqueCode();

        ShortUrl shortUrl = ShortUrl.builder()
                .originalUrl(request.originalUrl())
                .shortCode(shortCode)
                .expiresAt(request.expiresInDays() != null ? LocalDateTime.now().plusDays(request.expiresInDays()) : null).build();
        return toResponse(shortUrlRepository.save(shortUrl));
    }

    @Transactional
    @Cacheable(value = "urls", key = "#shortCode")
    public String resolve(String shortCode){
        ShortUrl shortUrl = findByCodeOrThrow(shortCode);

        if (shortUrl.isExpired()){
            throw new LinkExpiredException(shortCode);
        }

        shortUrlRepository.incrementClickCount(shortCode);
        return shortUrl.getOriginalUrl();
    }

    @Transactional(readOnly = true)
    public UrlResponse findStats(String shortCode){
        return toResponse(findByCodeOrThrow(shortCode));
    }

    @Transactional(readOnly = true)
    public List<UrlResponse> findAll(){
        return shortUrlRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    @CacheEvict(value = "urls", key = "#shortCode")
    public void delete (String shorCode){
        ShortUrl shortUrl = findByCodeOrThrow(shorCode);
        shortUrlRepository.delete(shortUrl);
    }

    private String generateUniqueCode(){
        String code;
        int attempts = 0;

        do {
            if (attempts++ > 10){
                throw new IllegalStateException("Não foi possível gerar um código único. Tente novamente mais tarde");
            }
            code = codeGenerator.generate();
        } while (shortUrlRepository.existsByShortCode(code));
        return code;
    }

    private ShortUrl findByCodeOrThrow(String shortCode){
        return shortUrlRepository.findByShortCode(shortCode).orElseThrow(() -> new ShortUrlNotFoundException(shortCode));
    }

    private UrlResponse toResponse(ShortUrl entity){
        return new UrlResponse(
                entity.getId(),
                entity.getShortCode(),
                baseUrl + "/" + entity.getShortCode(),
                entity.getOriginalUrl(),
                entity.getClickCount(),
                entity.getExpiresAt(),
                entity.getCreatedAt()
        );
    }
}
