package com.rickmorty.exceptions;

public class FavoriteAlreadyExists extends RuntimeException {
    public FavoriteAlreadyExists() {
        super("O favorito já está cadastrado");
    }
}
