package com.rickmorty.Models;

public class ValidationErrorResponse {
    private String errors;

    public ValidationErrorResponse(String errors) {
        this.errors = errors;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String message) {
        this.errors = message;
    }
}
