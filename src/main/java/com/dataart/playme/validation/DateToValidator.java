package com.dataart.playme.validation;

import com.dataart.playme.util.Constants;
import com.dataart.playme.util.DateUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

public class DateToValidator implements ConstraintValidator<DateTo, Date> {

    private Date to;

    @Override
    public void initialize(DateTo constraint) {
        if (constraint.to().equals(Constants.NOW)) {
            to = new Date(System.currentTimeMillis());
        } else {
            to = DateUtil.getDateFromString(constraint.to());
        }
    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        return value != null && to.after(value);
    }
}
