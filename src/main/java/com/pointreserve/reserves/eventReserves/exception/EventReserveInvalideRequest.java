package com.pointreserve.reserves.eventReserves.exception;

import com.pointreserve.reserves.common.exception.ReservesException;
import lombok.Getter;

@Getter
public class EventReserveInvalideRequest extends ReservesException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    public EventReserveInvalideRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
