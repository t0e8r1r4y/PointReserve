package com.pointreserve.reserves.eventDetail.exception;

import com.pointreserve.reserves.common.exception.ReservesException;

public class EventDetailNotFound extends ReservesException {

    private static final String MESSAGE = "존재하지 않는 상세정보입니다.";

    public EventDetailNotFound(){
        super(MESSAGE);
        addValidation("errorResponse", MESSAGE);
    }

    @Override
    public int statusCode() {
        return 404;
    }
}