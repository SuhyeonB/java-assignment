package com.example.assignment.global.exception;

public record ErrorResponse(ErrorBody error) {
    public record ErrorBody(String code, String message) {}
}
