package com.pointreserve.reserves.common.controller;

import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointConfilctException;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointInvalidRequestException;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointNotFoundException;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointErrorResponse;
import com.pointreserve.reserves.eventdetail.exception.EventDetailNotFoundException;
import com.pointreserve.reserves.eventreserves.exception.EventReserveInvalideRequestException;
import com.pointreserve.reserves.eventreserves.exception.EventReservesNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler(AccumulatedPointNotFoundException.class)
    public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse (AccumulatedPointNotFoundException e){

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
    @ExceptionHandler(EventReservesNotFoundException.class)
    public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse (EventReservesNotFoundException e){

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
    @ExceptionHandler(EventReserveInvalideRequestException.class)
    public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse (EventReserveInvalideRequestException e){

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
    @ExceptionHandler(EventDetailNotFoundException.class)
    public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse (EventDetailNotFoundException e){

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
    public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse (AccumulatedPointInvalidRequestException e){

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
    public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse (AccumulatedPointConfilctException e){

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