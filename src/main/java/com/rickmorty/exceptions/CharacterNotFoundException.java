package com.rickmorty.exceptions;

public class CharacterNotFoundException extends RuntimeException {
    public CharacterNotFoundException() {
        super("Character não encontrado para o ID");
    }
}
