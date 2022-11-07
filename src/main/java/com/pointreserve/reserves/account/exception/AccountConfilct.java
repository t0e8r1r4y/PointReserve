package com.pointreserve.reserves.account.exception;

import com.pointreserve.reserves.common.exception.ReservesException;
import lombok.Getter;

@Getter
public class AccountConfilct  extends ReservesException {
    private static final String MESSAGE = "이미 존재하는 계정입니다. (향후 하나의 계정이 여러 포인트를 사용하도록 개선 예정입니다.)";

    public AccountConfilct() {
        super(MESSAGE);
        addValidation("errorResponse", MESSAGE);
    }

    @Override
    public int statusCode() {
        return 409;
    }
}
