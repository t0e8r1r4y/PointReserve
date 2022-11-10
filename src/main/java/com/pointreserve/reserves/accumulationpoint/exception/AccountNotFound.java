package com.pointreserve.reserves.accumulationpoint.exception;

import com.pointreserve.reserves.common.exception.ReservesException;

public class AccountNotFound extends ReservesException {

    private static final String MESSAGE = "존재하지 않는 계좌입니다.";

    public AccountNotFound() {
        super(MESSAGE);
        addValidation("errorMessage", MESSAGE);
    }

    @Override
    public int statusCode() {
        return 404;
    }
}
