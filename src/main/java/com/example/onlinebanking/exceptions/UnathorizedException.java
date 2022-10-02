package com.example.onlinebanking.exceptions;

public class UnathorizedException extends RuntimeException {
    public UnathorizedException(String message) {
        super(message);
    }
}
