package com.pointreserve.reserves.eventReserves.exception;


import com.pointreserve.reserves.common.exception.ReservesException;

public class EventReservesNotFoundException extends ReservesException {

    private static final String MESSAGE = "존재하지 않는 Event입니다.";

    public EventReservesNotFoundException() {
        super(MESSAGE);
        addValidation("errorResponse", MESSAGE);
    }


    @Override
    public int statusCode() {
        return 404;
    }
}
