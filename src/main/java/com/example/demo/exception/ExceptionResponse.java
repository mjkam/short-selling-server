package com.example.demo.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ExceptionResponse {
    private String code;

    public ExceptionResponse(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
    }
}
