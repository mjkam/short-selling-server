package com.example.demo.exception;

public class ClientBadRequestException extends RuntimeException {
    public ClientBadRequestException(String msg) {
        super(msg);
    }
}
