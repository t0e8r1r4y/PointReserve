package com.pointreserve.reserves.common.exception;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class ReservesException extends RuntimeException{
    private final Map<String, String> validation = new HashMap<>();

    public ReservesException(String message) {
        super(message);
    }

    public ReservesException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int statusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
