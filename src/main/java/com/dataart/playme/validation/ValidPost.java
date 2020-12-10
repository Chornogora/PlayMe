package com.dataart.playme.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PostValidator.class)
@Documented
public @interface ValidPost {

    String message() default "Empty post";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
