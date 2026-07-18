package com.sprinttracker.common;

public class AccessDeniedApplicationException extends RuntimeException {
    public AccessDeniedApplicationException(String message) {
        super(message);
    }
}
