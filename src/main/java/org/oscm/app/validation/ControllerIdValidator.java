package org.oscm.app.validation;

import org.oscm.app.domain.enumeration.Controller;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class ControllerIdValidator
        implements ConstraintValidator<ControllerId, String> {

    @Override
    public void initialize(ControllerId constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        Stream<Controller> controllers = Arrays.stream(Controller.values());
        Optional<Controller> controller = controllers
                .filter(c -> c.getControllerId().equals(value)).findFirst();

        return controller.isPresent();
    }
}
