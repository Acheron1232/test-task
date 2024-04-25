package com.acheron.testtask.validation;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BirthDateValidatorTest {

    private final BirthDateValidator validator = new BirthDateValidator();

    @Test
    void isValidShouldReturnTrueForPastDate() {
        LocalDate dateInPast = LocalDate.now().minusYears(1);
        boolean result = validator.isValid(dateInPast, null);
        assertTrue(result);
    }

    @Test
    void isValidShouldReturnFalseForFutureDate() {
        LocalDate dateInFuture = LocalDate.now().plusDays(1);
        boolean result = validator.isValid(dateInFuture, null);
        assertFalse(result);
    }

    @Test
    void isValidShouldReturnFalseForCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        boolean result = validator.isValid(currentDate, null);
        assertFalse(result);
    }
}
