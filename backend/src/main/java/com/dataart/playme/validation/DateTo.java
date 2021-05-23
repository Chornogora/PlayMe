package com.dataart.playme.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateToValidator.class)
@Documented
public @interface DateTo {

    String message() default "late_date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String to();
}
