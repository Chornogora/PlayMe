package com.dataart.playme.validation;

import com.dataart.playme.dto.CreatePostDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PostValidator implements ConstraintValidator<ValidPost, CreatePostDto> {

    @Override
    public boolean isValid(CreatePostDto dto, ConstraintValidatorContext context) {
        return dto.getText() != null || dto.getFile() != null || dto.getPhoto() != null;
    }
}