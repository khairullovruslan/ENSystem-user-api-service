package org.apiservice.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apiservice.util.EmailForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailValidationServiceTest {

    @Mock
    private Validator validator;

    @InjectMocks
    private EmailValidationService emailValidationService;

    @Test
    @DisplayName("Метод IsValidEmail вызывает метод validate у  валидатора и возвращает true, если почта корректная")
    void testIsValidEmail_ValidEmail() {
        String validEmail = "test@example.com";
        EmailForm emailForm = new EmailForm();
        emailForm.setEmail(validEmail);

        when(validator.validate(argThat((EmailForm ef) -> ef.getEmail().equals(validEmail)))).thenReturn(Collections.emptySet());


        assertTrue(emailValidationService.isValidEmail(validEmail));
    }

    @Test
    @DisplayName("Метод IsValidEmail вызывает метод validate у  валидатора и возвращает false, если почта некорректная")
    void testIsValidEmail_InvalidEmail() {
        String invalidEmail = "testmail.ru";
        EmailForm emailForm = new EmailForm();
        emailForm.setEmail(invalidEmail);
        ConstraintViolation<EmailForm> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<EmailForm>> violations = Collections.singleton(violation);

        when(validator.validate(argThat((EmailForm ef) -> ef.getEmail().equals(invalidEmail)))).thenReturn(violations);


        assertFalse(emailValidationService.isValidEmail(invalidEmail));
    }
}
