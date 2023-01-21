package com.example.demo.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalControllerAdvice {
    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getFieldError() != null ?
                getMessageFromCodes(ex.getFieldError()) : ex.getMessage();
        log.info(errorMessage);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(ExceptionCode.BAD_REQUEST));
    }

    private String getMessageFromCodes(ObjectError objectError) {
        if (objectError.getCodes() == null) {
            return objectError.getDefaultMessage();
        }
        return Arrays.stream(objectError.getCodes())
                .map(code -> extractMessage(code, objectError.getArguments()))
                .filter(Objects::nonNull)
                .findAny()
                .orElseGet(objectError::getDefaultMessage);
    }

    private String extractMessage(String code, Object[] arguments) {
        try {
            return messageSource.getMessage(code, arguments, Locale.getDefault());
        } catch (NoSuchMessageException ex) {
            return null;
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.info(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(ExceptionCode.BAD_REQUEST));
    }
}
