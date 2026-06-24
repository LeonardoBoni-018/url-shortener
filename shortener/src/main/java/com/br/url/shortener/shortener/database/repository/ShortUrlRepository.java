package com.br.url.shortener.shortener.database.repository;

import com.br.url.shortener.shortener.database.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, UUID> {
    Optional<ShortUrl> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);

    @Modifying
    @Transactional
    @Query("UPDATE ShortUrl s SET s.clickCount = s.clickCount + 1 WHERE s.shortCode = :shortCode")
    void incrementClickCount(@Param("shortCode") String shortCode);
}
