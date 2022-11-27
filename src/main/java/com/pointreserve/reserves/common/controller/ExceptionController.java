package com.pointreserve.reserves.common.controller;

import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointConfilctException;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointInvalidRequestException;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointNotFoundException;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointErrorResponse;
import com.pointreserve.reserves.pointdetail.exception.PointDetailNotFoundException;
import com.pointreserve.reserves.point.exception.PointInvalideRequestException;
import com.pointreserve.reserves.point.exception.PointNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

  @ResponseBody
  @ExceptionHandler(AccumulatedPointNotFoundException.class)
  public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse(
      AccumulatedPointNotFoundException e) {

    int statusCode = e.statusCode();

    AccumulatedPointErrorResponse body = AccumulatedPointErrorResponse.builder()
        .code(String.valueOf(statusCode))
        .message(e.getMessage())
        .validation(e.getValidation())
        .build();

    return ResponseEntity.status(statusCode)
        .body(body);
  }

  @ResponseBody
  @ExceptionHandler(PointNotFoundException.class)
  public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse(PointNotFoundException e) {

    int statusCode = e.statusCode();

    AccumulatedPointErrorResponse body = AccumulatedPointErrorResponse.builder()
        .code(String.valueOf(statusCode))
        .message(e.getMessage())
        .validation(e.getValidation())
        .build();

    return ResponseEntity.status(statusCode)
        .body(body);
  }

  @ResponseBody
  @ExceptionHandler(PointInvalideRequestException.class)
  public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse(
      PointInvalideRequestException e) {

    int statusCode = e.statusCode();

    AccumulatedPointErrorResponse body = AccumulatedPointErrorResponse.builder()
        .code(String.valueOf(statusCode))
        .message(e.getMessage())
        .validation(e.getValidation())
        .build();

    return ResponseEntity.status(statusCode)
        .body(body);
  }

  @ResponseBody
  @ExceptionHandler(PointDetailNotFoundException.class)
  public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse(
      PointDetailNotFoundException e) {

    int statusCode = e.statusCode();

    AccumulatedPointErrorResponse body = AccumulatedPointErrorResponse.builder()
        .code(String.valueOf(statusCode))
        .message(e.getMessage())
        .validation(e.getValidation())
        .build();

    return ResponseEntity.status(statusCode)
        .body(body);
  }

  @ResponseBody
  @ExceptionHandler(AccumulatedPointInvalidRequestException.class)
  public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse(
      AccumulatedPointInvalidRequestException e) {

    int statusCode = e.statusCode();

    AccumulatedPointErrorResponse body = AccumulatedPointErrorResponse.builder()
        .code(String.valueOf(statusCode))
        .message(e.getMessage())
        .validation(e.getValidation())
        .build();

    return ResponseEntity.status(statusCode)
        .body(body);
  }

  @ResponseBody
  @ExceptionHandler(AccumulatedPointConfilctException.class)
  public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse(
      AccumulatedPointConfilctException e) {

    int statusCode = e.statusCode();

    AccumulatedPointErrorResponse body = AccumulatedPointErrorResponse.builder()
        .code(String.valueOf(statusCode))
        .message(e.getMessage())
        .validation(e.getValidation())
        .build();

    return ResponseEntity.status(statusCode)
        .body(body);
  }
}