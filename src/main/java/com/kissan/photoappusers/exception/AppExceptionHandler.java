package com.kissan.photoappusers.exception;

import com.kissan.photoappusers.ui.model.response.ExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request){
        System.out.println("ex = " + ex + ", request = " + request);
        ExceptionResponse exceptionResponse = new ExceptionResponse(OffsetDateTime.now(),
                "",
                ex.getLocalizedMessage() == null ? ex.getMessage() : ex.getLocalizedMessage()
                );

        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {NullPointerException.class, UserException.class})
    public ResponseEntity<Object> handleSpecificException(Exception ex, WebRequest request){
        System.out.println("ex = " + ex + ", request = " + request);
        ExceptionResponse exceptionResponse = new ExceptionResponse(OffsetDateTime.now(),
                "",
                ex.getLocalizedMessage() == null ? ex.getMessage() : ex.getLocalizedMessage()
        );

        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
