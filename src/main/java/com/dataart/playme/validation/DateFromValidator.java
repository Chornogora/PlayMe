package com.dataart.playme.validation;

import com.dataart.playme.util.DateUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

public class DateFromValidator implements ConstraintValidator<DateFrom, Date> {

    private Date from;

    @Override
    public void initialize(DateFrom constraint) {
        from = DateUtil.getDateFromString(constraint.from());
    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        return value != null && from.before(value);
    }
}