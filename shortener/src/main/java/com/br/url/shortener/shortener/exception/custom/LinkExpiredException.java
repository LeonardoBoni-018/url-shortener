package com.br.url.shortener.shortener.exception.custom;

public class LinkExpiredException extends  RuntimeException{
    public LinkExpiredException(String shortCode){
        super("O link com código '" + shortCode + "' expirou e não está mais disponível.");
    }
}
