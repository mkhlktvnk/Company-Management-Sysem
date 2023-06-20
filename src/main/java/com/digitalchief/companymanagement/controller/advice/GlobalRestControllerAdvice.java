package com.digitalchief.companymanagement.controller.advice;

import com.digitalchief.companymanagement.model.ErrorResponse;
import com.digitalchief.companymanagement.service.exception.EntityNotFoundException;
import com.digitalchief.companymanagement.service.exception.EntityNotUniqueException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalRestControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            HttpServletRequest request, EntityNotFoundException e) {
        ErrorResponse response = ErrorResponse.builder()
                .message(e.getMessage())
                .url(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(EntityNotUniqueException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotUniqueException(
            HttpServletRequest request, EntityNotUniqueException e) {
        ErrorResponse response = ErrorResponse.builder()
                .message(e.getMessage())
                .url(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
