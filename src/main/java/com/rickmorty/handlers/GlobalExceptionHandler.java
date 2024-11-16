package com.rickmorty.handlers;

import com.rickmorty.Models.CustomErrorResponse;
import com.rickmorty.exceptions.*;
import com.rickmorty.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.View;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handlerUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PageNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handlePageNotFoundException(PageNotFoundException ex) {
        return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidParameterException(InvalidParameterException ex) {
        return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidIdException(InvalidIdException ex) {
        return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleResourceNotFoundException(LocationNotFoundException ex) {
        return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RewriteErrorException.class)
    public ResponseEntity<CustomErrorResponse> handleRewriteErrorException(RewriteErrorException ex) {
        return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String message = "Parâmetro " + ex.getName() + " inválido";
        return new ResponseEntity<>(new CustomErrorResponse(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        return new ResponseEntity<>(new CustomErrorResponse("Rota inexistente"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("A rota " + ex.getRequestURL() + " não foi encontrada.");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CustomErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(CharacterNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleCharacterNotFoundException(CharacterNotFoundException ex) {
        return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PageNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handlePageNotFound(PageNotFoundException ex) {
        return new ResponseEntity<>(new CustomErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidParameterException(InvalidParameterException ex) {
        CustomErrorResponse error = new CustomErrorResponse(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleException(Exception ex) {
        log.error("Um erro inesperado aconteceu" + ex.getMessage());
        return new ResponseEntity<>(new CustomErrorResponse("Ocorreu um erro inesperado."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
