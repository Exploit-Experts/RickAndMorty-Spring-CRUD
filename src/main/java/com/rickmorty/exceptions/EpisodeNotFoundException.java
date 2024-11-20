package com.rickmorty.exceptions;

public class EpisodeNotFoundException extends RuntimeException {
    public EpisodeNotFoundException() {
        super("Episode não encontrado para o ID");
    }

}
