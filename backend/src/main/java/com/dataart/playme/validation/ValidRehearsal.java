package com.dataart.playme.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RehearsalValidator.class)
@Documented
public @interface ValidRehearsal {

    String message() default "Invalid date entered";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
