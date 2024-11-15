package com.rickmorty.exceptions;

public class InvalidPageNumberException extends RuntimeException {
    public InvalidPageNumberException() {
    super("Valor da pagina é invalido");
    }
    public InvalidPageNumberException(String message) {
        super(message);
    }
}