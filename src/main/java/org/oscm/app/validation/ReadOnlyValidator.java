package org.oscm.app.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReadOnlyValidator
        implements ConstraintValidator<ReadOnly, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null;
    }
}
