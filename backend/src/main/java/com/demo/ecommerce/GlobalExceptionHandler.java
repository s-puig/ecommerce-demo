package com.demo.ecommerce;

import com.demo.ecommerce.common.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

class RuntimeError {
    String type;
    String message;
    Instant timestamp;

    public RuntimeError(String type, String message, Instant timestamp) {
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
    }
}

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RuntimeError("Resource Not Found", exception.getMessage(), Instant.now()));
    }
}
