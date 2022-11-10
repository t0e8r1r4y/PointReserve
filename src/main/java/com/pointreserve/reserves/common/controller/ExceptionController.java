package com.pointreserve.reserves.common.controller;

import com.pointreserve.reserves.accumulationpoint.exception.AccountConfilct;
import com.pointreserve.reserves.accumulationpoint.exception.AccountInvalidRequest;
import com.pointreserve.reserves.accumulationpoint.exception.AccountNotFound;
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
    @ExceptionHandler(AccountNotFound.class)
    public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse (AccountNotFound e){

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
    @ExceptionHandler(AccountInvalidRequest.class)
    public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse (AccountInvalidRequest e){

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
    @ExceptionHandler(AccountConfilct.class)
    public ResponseEntity<AccumulatedPointErrorResponse> ErrorResponse (AccountConfilct e){

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