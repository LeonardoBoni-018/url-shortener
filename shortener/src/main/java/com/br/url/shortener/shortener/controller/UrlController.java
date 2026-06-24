package com.br.url.shortener.shortener.controller;
import com.br.url.shortener.shortener.dto.request.CreateUrlRequest;
import com.br.url.shortener.shortener.dto.response.UrlResponse;
import com.br.url.shortener.shortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "URLs", description = "Gerenciamento de links encurtados")
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/api/urls")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Link criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "URL inválida ou parâmetros incorretos")
    })
    public UrlResponse create(@RequestBody @Valid CreateUrlRequest request){
        return urlService.create(request);
    }

    @GetMapping("/{shortCode}")
    @Operation(summary = "Redireciona para a URL original (302)")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Redirecionamento realizado"),
            @ApiResponse(responseCode = "404", description = "Código não encontrado"),
            @ApiResponse(responseCode = "410", description = "Link expirado")
    })
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = urlService.resolve(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    @GetMapping("/api/urls/{shortCode}/stats")
    @Operation(summary = "Retorna estatísticas de um link")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estatísticas retornadas"),
            @ApiResponse(responseCode = "404", description = "Código não encontrado")
    })
    public UrlResponse stats(@PathVariable String shortCode) {
        return urlService.findStats(shortCode);
    }

    @GetMapping("/api/urls")
    @Operation(summary = "Lista todos os links criados")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<UrlResponse> findAll() {
        return urlService.findAll();
    }

    @DeleteMapping("/api/urls/{shortCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove um link encurtado")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Link removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Código não encontrado")
    })
    public void delete(@PathVariable String shortCode) {
        urlService.delete(shortCode);
    }
}
