package com.dataart.playme.validation;

import com.dataart.playme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueValidator implements ConstraintValidator<Unique, String> {
    
    private final UserRepository userRepository;

    @Autowired
    public UniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isValid(String obj, ConstraintValidatorContext context) {
        return userRepository.findByLoginOrEmail(obj, obj).isEmpty();
    }
}
