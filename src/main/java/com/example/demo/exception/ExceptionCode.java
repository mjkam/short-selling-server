package com.example.demo.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    BAD_REQUEST("INVALID_REQUEST"),
    ;

    private final String code;

    ExceptionCode(String code) {
        this.code = code;
    }
}
