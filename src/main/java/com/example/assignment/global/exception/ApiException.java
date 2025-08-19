package com.example.assignment.global.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{
    private final ApiErrorCode code;
    private final HttpStatus status;

    public ApiException(ApiErrorCode code, String message) {
        super(message);
        this.code = code;
        this.status = switch (code) {
            case USER_ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case INVALID_CREDENTIALS, INVALID_TOKEN -> HttpStatus.UNAUTHORIZED;
            case ACCESS_DENIED -> HttpStatus.FORBIDDEN;
            case NOT_FOUND_USER -> HttpStatus.NOT_FOUND;
        };
    }
    public ApiErrorCode getCode() { return code; }
    public HttpStatus getStatus() { return status; }
}
