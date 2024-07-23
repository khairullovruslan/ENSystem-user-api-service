package org.apiservice.services;

import org.apiservice.util.EmailForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

@Service
public class EmailValidationService {

    private final Validator validator;

    @Autowired
    public EmailValidationService(Validator validator) {
        this.validator = validator;
    }

    public boolean isValidEmail(String email) {
        EmailForm emailForm = new EmailForm();
        emailForm.setEmail(email);
        Set<ConstraintViolation<EmailForm>> violations = validator.validate(emailForm);

        return violations.isEmpty();
    }
}
