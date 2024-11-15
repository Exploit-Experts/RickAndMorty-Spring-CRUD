package com.rickmorty.exceptions;

public class InvalidIdException extends RuntimeException {
    public InvalidIdException() {
        super("ID inválido: deve conter apenas números inteiros.");
    }
}

