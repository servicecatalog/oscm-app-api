package org.oscm.app.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ControllerIdValidator.class)
public @interface ControllerId {
    String message() default "must be one of [ess.aws, ess.openstack, ess.azure, ess.vmware]";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
