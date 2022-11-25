package com.pointreserve.reserves.point.exception;

import com.pointreserve.reserves.common.exception.ReservesException;
import lombok.Getter;

@Getter
public class PointInvalideRequestException extends ReservesException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    public PointInvalideRequestException(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
