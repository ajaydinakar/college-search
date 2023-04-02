package com.ajay.collegesearch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

//@RestControllerAdvice
public class CustomizedExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) throws Exception
    {
        ExceptionResponse response=new ExceptionResponse(new Date(),ex.getMessage(),request.getDescription(false));
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @ExceptionHandler(NoDetailsFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(Exception ex, WebRequest request) throws Exception
    {
        ExceptionResponse response=new ExceptionResponse(new Date(),ex.getMessage(),request.getDescription(false));
        return new ResponseEntity(response,HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(BadInputException.class)
    public final ResponseEntity<Object> handleBadInput(Exception ex, WebRequest request) throws Exception
    {
        ExceptionResponse response=new ExceptionResponse(new Date(),ex.getMessage(),request.getDescription(false));
        return new ResponseEntity(response,HttpStatus.BAD_REQUEST);

    }
}
