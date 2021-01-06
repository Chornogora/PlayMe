package com.dataart.playme.validation;

import com.dataart.playme.dto.CreatePostDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class PostValidator implements ConstraintValidator<ValidPost, CreatePostDto> {

    @Override
    public boolean isValid(CreatePostDto dto, ConstraintValidatorContext context) {
        return textNotEmpty(dto.getText()) || filesPresent(dto.getFiles()) || filesPresent(dto.getPhotos());
    }

    private boolean textNotEmpty(String text) {
        return text != null && text.length() > 0;
    }

    private boolean filesPresent(List<?> files) {
        return files != null && files.size() > 0;
    }
}
