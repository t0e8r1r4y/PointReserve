package com.pointreserve.reserves.point.exception;


import com.pointreserve.reserves.common.exception.ReservesException;

public class PointNotFoundException extends ReservesException {

  private static final String MESSAGE = "존재하지 않는 Event입니다.";

  public PointNotFoundException() {
    super(MESSAGE);
    addValidation("errorResponse", MESSAGE);
  }


  @Override
  public int statusCode() {
    return 404;
  }
}
