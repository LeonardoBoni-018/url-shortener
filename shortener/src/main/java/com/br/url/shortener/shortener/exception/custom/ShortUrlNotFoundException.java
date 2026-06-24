package com.br.url.shortener.shortener.exception.custom;

public class ShortUrlNotFoundException extends RuntimeException{
    public ShortUrlNotFoundException(String shortCode){
        super("Nenhum link encontrado para o código: " + shortCode);
    }
}
