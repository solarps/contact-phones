package com.testtask.phonecontacts.controller.validation;

import com.testtask.phonecontacts.model.Error;
import com.testtask.phonecontacts.model.enums.ErrorType;
import com.testtask.phonecontacts.service.exceptions.EntityExistsException;
import com.testtask.phonecontacts.service.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public List<Error> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("handleMethodArgumentNotValidException: {}", exception.getMessage(), exception);
        return exception.getBindingResult().getAllErrors().stream()
                .map(err -> new Error(err.getDefaultMessage(),
                        ErrorType.VALIDATION_ERROR_TYPE,
                        LocalDateTime.now()))
                .toList();
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handleEntityExistsException(EntityExistsException exception) {
        return new Error(exception.getMessage(), exception.getErrorType(), LocalDateTime.now());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleEntityNotFoundException(EntityNotFoundException exception) {
        return new Error(exception.getMessage(), exception.getErrorType(), LocalDateTime.now());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleSQLException(SQLException exception) {
        return new Error(exception.getMessage(), ErrorType.DATABASE_ERROR_TYPE, LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handleException(Exception exception) {
        return new Error(exception.getMessage(), ErrorType.FATAL_ERROR_TYPE, LocalDateTime.now());
    }
}
