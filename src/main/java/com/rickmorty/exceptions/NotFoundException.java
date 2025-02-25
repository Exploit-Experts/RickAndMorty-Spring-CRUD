package com.rickmorty.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Não encontrado");
    }
}