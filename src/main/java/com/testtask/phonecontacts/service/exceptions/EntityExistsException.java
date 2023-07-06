package com.testtask.phonecontacts.service.exceptions;

import com.testtask.phonecontacts.model.enums.ErrorType;

public class EntityExistsException extends ServiceException {

    private static final String DEFAULT_MESSAGE = "Entity already exists";

    public EntityExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public EntityExistsException(String message) {
        super(message);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.VALIDATION_ERROR_TYPE;
    }
}
