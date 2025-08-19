package com.example.assignment.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApi(ApiException e) {
        var body = new ErrorResponse.ErrorBody(e.getCode().name(), e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(body));
    }
}
