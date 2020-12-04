package com.dataart.playme.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateFromValidator.class)
@Documented
public @interface DateFrom {

    String message() default "early_date";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String from();
}
