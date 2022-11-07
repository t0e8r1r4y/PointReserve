package com.pointreserve.reserves.common.controller;

import com.pointreserve.reserves.account.exception.AccountConfilct;
import com.pointreserve.reserves.account.exception.AccountInvalidRequest;
import com.pointreserve.reserves.account.exception.AccountNotFound;
import com.pointreserve.reserves.account.ui.dto.ErrorResponse;
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
    public ResponseEntity<ErrorResponse> ErrorResponse (AccountNotFound e){

        int statusCode = e.statusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();


        return ResponseEntity.status(statusCode)
                .body(body);
    }

    @ResponseBody
    @ExceptionHandler(EventReservesNotFound.class)
    public ResponseEntity<ErrorResponse> ErrorResponse (EventReservesNotFound e){

        int statusCode = e.statusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();


        return ResponseEntity.status(statusCode)
                .body(body);
    }

    @ResponseBody
    @ExceptionHandler(EventReserveInvalideRequest.class)
    public ResponseEntity<ErrorResponse> ErrorResponse (EventReserveInvalideRequest e){

        int statusCode = e.statusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();


        return ResponseEntity.status(statusCode)
                .body(body);
    }

    @ResponseBody
    @ExceptionHandler(EventDetailNotFound.class)
    public ResponseEntity<ErrorResponse> ErrorResponse (EventDetailNotFound e){

        int statusCode = e.statusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();


        return ResponseEntity.status(statusCode)
                .body(body);
    }

    @ResponseBody
    @ExceptionHandler(AccountInvalidRequest.class)
    public ResponseEntity<ErrorResponse> ErrorResponse (AccountInvalidRequest e){

        int statusCode = e.statusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();


        return ResponseEntity.status(statusCode)
                .body(body);
    }

    @ResponseBody
    @ExceptionHandler(AccountConfilct.class)
    public ResponseEntity<ErrorResponse> ErrorResponse (AccountConfilct e){

        int statusCode = e.statusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();


        return ResponseEntity.status(statusCode)
                .body(body);
    }
}