package com.dataart.playme.validation;

import com.dataart.playme.dto.RehearsalDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

public class RehearsalValidator implements ConstraintValidator<ValidRehearsal, RehearsalDto> {

    @Override
    public boolean isValid(RehearsalDto value, ConstraintValidatorContext context) {
        return value.getStartDatetime().compareTo(new Date(System.currentTimeMillis())) > 0
                && value.getFinishDatetime().compareTo(value.getStartDatetime()) > 0;
    }
}
