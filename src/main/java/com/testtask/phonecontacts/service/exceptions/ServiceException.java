package com.testtask.phonecontacts.service.exceptions;

import com.testtask.phonecontacts.model.enums.ErrorType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceException extends RuntimeException {
    private static final ErrorType ERROR_TYPE = ErrorType.FATAL_ERROR_TYPE;

    public ServiceException(String message) {
        super(message);
    }

    public ErrorType getErrorType() {
        return ERROR_TYPE;
    }
}
