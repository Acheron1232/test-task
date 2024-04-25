package com.acheron.testtask.validation;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BirthDateValidatorTest {

    private final BirthDateValidator validator = new BirthDateValidator();

    @Test
    void isValidShouldReturnTrueForPastDate() {
        // Arrange
        LocalDate dateInPast = LocalDate.now().minusYears(1);

        // Act
        boolean result = validator.isValid(dateInPast, null);

        // Assert
        assertTrue(result, "Validator should return true for dates in the past");
    }

    @Test
    void isValidShouldReturnFalseForFutureDate() {
        // Arrange
        LocalDate dateInFuture = LocalDate.now().plusDays(1);

        // Act
        boolean result = validator.isValid(dateInFuture, null);

        // Assert
        assertFalse(result, "Validator should return false for dates in the future");
    }

    @Test
    void isValidShouldReturnFalseForCurrentDate() {
        // Arrange
        LocalDate currentDate = LocalDate.now();

        // Act
        boolean result = validator.isValid(currentDate, null);

        // Assert
        assertFalse(result, "Validator should return false for the current date");
    }
}
