package com.rickmorty.handlers;

import com.rickmorty.Models.CustomErrorResponse;
import com.rickmorty.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handlerUserNotFound(UserNotFoundException ex) {
        CustomErrorResponse error = new CustomErrorResponse(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PageNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handlePageNotFoundException(PageNotFoundException ex) {
        CustomErrorResponse error = new CustomErrorResponse(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidParameterException(InvalidParameterException ex) {
        CustomErrorResponse error = new CustomErrorResponse(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidIdException(InvalidIdException ex) {
        CustomErrorResponse error = new CustomErrorResponse(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleResourceNotFoundException(LocationNotFoundException ex) {
        CustomErrorResponse error = new CustomErrorResponse(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RewriteErrorException.class)
    public ResponseEntity<CustomErrorResponse> handleRewriteErrorException(RewriteErrorException ex) {
        CustomErrorResponse error = new CustomErrorResponse(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String message = "Parâmetro " + ex.getName() + " inválido";
        CustomErrorResponse error = new CustomErrorResponse(message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<CustomErrorResponse> handleIOException(Exception ex) {
        CustomErrorResponse error = new CustomErrorResponse("Ocorreu um erro inesperado.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<CustomErrorResponse> handleGenericException(GenericException ex) {
        log.error("Um erro inesperado aconteceu" + ex.getMessage());
        CustomErrorResponse error = new CustomErrorResponse("Ocorreu um erro inesperado.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
