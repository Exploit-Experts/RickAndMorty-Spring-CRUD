package com.rickmorty.exceptions;

public class FavoriteNotFound extends RuntimeException {
    public FavoriteNotFound() {
        super("Favorito não encontrado");
    }

    public FavoriteNotFound(String message) {
        super(message);
    }
}
