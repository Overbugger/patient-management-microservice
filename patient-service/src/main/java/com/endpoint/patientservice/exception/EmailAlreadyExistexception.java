package com.endpoint.patientservice.exception;

public class EmailAlreadyExistexception extends RuntimeException {
    public EmailAlreadyExistexception(String message) {
        super(message);
    }
}
