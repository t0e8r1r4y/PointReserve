package com.pointreserve.reserves.common.exception;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class ReservesException extends RuntimeException{
    private final Map<String, String> validation = new HashMap<>();

    protected ReservesException(String message) {
        super(message);
    }

    protected ReservesException(String message, Throwable cause) {
        super(message, cause);
    }

    protected abstract int statusCode();

    protected void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
