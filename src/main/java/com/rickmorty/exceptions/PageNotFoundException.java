package com.rickmorty.exceptions;

public class PageNotFoundException extends RuntimeException {
    public PageNotFoundException() {
        super("Pagina não encontrada");
    }
}
