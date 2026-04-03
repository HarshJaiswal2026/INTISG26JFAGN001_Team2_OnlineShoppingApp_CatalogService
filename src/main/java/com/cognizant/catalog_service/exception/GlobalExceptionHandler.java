package com.cognizant.catalog_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        String message = (ex.getMessage() != null) ? ex.getMessage() : "An unexpected error occurred";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // 1. Refined Status Selection
        if (message.toLowerCase().contains("unauthorized")) {
            status = HttpStatus.UNAUTHORIZED; // 401
        } else if (message.toLowerCase().contains("not found")) {
            status = HttpStatus.NOT_FOUND; // 404
        }

        // 2. Build Response Body
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", java.time.LocalDateTime.now().toString());
        errorResponse.put("error", message);
        errorResponse.put("status", status.value());
        errorResponse.put("path", "Check your request URL"); // Optional: for debugging

        // 3. Log to console so you can see it's working
        System.out.println("[Exception Handler] Caught: " + message + " -> Sending Status: " + status);

        return new ResponseEntity<>(errorResponse, status);
    }
}