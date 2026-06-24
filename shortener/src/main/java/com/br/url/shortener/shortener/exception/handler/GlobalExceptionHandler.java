package com.br.url.shortener.shortener.exception.handler;

import com.br.url.shortener.shortener.dto.response.ErrorResponse;
import com.br.url.shortener.shortener.exception.custom.LinkExpiredException;
import com.br.url.shortener.shortener.exception.custom.ShortUrlNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ShortUrlNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ShortUrlNotFoundException ex) {
    return new ErrorResponse("NOT_FOUND", ex.getMessage());
}

    @ExceptionHandler(LinkExpiredException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ErrorResponse handleExpired(LinkExpiredException ex) {
        return new ErrorResponse("LINK_EXPIRED", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return new ErrorResponse("VALIDATION_ERROR", message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(Exception ex) {
        return new ErrorResponse("INTERNAL_ERROR", "Ocorreu um erro inesperado. Tente novamente.");
    }
}
