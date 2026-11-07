package com.ullas.University.management.service;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Resource not found");
    }
}
