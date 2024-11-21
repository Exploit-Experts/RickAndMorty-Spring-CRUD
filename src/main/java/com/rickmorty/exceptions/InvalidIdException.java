package com.rickmorty.exceptions;

public class InvalidIdException extends RuntimeException {
    public InvalidIdException() {
        super("ID enviado inválido, o id deve ser um número inteiro maior ou igual a 1");
    }
}
