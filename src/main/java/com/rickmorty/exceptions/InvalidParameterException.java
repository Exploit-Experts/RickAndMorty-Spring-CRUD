package com.rickmorty.exceptions;

public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException() {
        super("O numero da pagina não pode ser negativo");
    }
}