package com.rickmorty.exceptions;

public class RewriteErrorException extends RuntimeException {
    public RewriteErrorException(String message) {
        super(message);
    }
}
