package com.speer.assessment.handler;

import jakarta.servlet.ServletException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Arrays;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e){
        return ResponseEntity.internalServerError().body("Internal Server error");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ServletException.class)
    public ResponseEntity<String> handleServletException(ServletException servletException){
        return ResponseEntity.badRequest().body("Either token is expired or invalid");
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e){
        return ResponseEntity.badRequest().body("Either token is expired or invalid"+ e);
    }

}
