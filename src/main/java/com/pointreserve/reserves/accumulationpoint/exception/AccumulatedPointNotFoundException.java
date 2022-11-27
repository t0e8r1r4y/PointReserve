package com.pointreserve.reserves.accumulationpoint.exception;

import com.pointreserve.reserves.common.exception.ReservesException;

public class AccumulatedPointNotFoundException extends ReservesException {

  private static final String MESSAGE = "존재하지 않는 계좌입니다.";

  public AccumulatedPointNotFoundException() {
    super(MESSAGE);
    addValidation("errorMessage", MESSAGE);
  }

  @Override
  public int statusCode() {
    return 404;
  }
}
