package com.pointreserve.reserves.common.controller;

import com.pointreserve.reserves.accumulationpoint.exception.AccountConfilctException;
import com.pointreserve.reserves.accumulationpoint.exception.AccountInvalidRequestException;
import com.pointreserve.reserves.accumulationpoint.exception.AccountNotFoundException;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointErrorResponse;
import com.pointreserve.reserves.eventDetail.exception.EventDetailNotFound;
import com.pointreserve.reserves.eventReserves.exception.EventReserveInvalideRequest;
import com.pointreserve.reserves.eventReserves.exception.EventReservesNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse (AccountNotFoundException e){

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
    @ExceptionHandler(EventReservesNotFound.class)
    public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse (EventReservesNotFound e){

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
    @ExceptionHandler(EventReserveInvalideRequest.class)
    public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse (EventReserveInvalideRequest e){

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
    @ExceptionHandler(EventDetailNotFound.class)
    public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse (EventDetailNotFound e){

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
    @ExceptionHandler(AccountInvalidRequestException.class)
    public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse (AccountInvalidRequestException e){

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
    @ExceptionHandler(AccountConfilctException.class)
    public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse (AccountConfilctException e){

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