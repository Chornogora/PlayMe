package com.dataart.playme.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
@Documented
public @interface Unique {

    String message() default "user_exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
