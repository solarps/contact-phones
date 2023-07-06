package com.testtask.phonecontacts.model.validation.validator;

import com.testtask.phonecontacts.model.validation.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(
            "^\\+?[1-9]\\d{1,14}$"
    );

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && PHONE_NUMBER_PATTERN.matcher(value).matches();
    }
}
