package com.mtiteiu.clinic.unit.validation;

import com.mtiteiu.clinic.validation.CNPValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class CNPValidatorTest {
    private final CNPValidator cnpValidator = new CNPValidator();
    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    static Stream<Arguments> cnpProvider() {
        return Stream.of(
                Arguments.of("1800101221144", true),  // Valid CNP example
                Arguments.of("123456789", false),     // Invalid length
                Arguments.of("12A4567890123", false), // Contains non-numeric characters
                Arguments.of("1800101221143", false), // Invalid control digit
                Arguments.of("1800131221144", false), // Invalid date
                Arguments.of("1800103211144", false), // Invalid month/day combination
                Arguments.of("1800102931144", false), // Invalid day for February
                Arguments.of("1800100431144", false), // Invalid day for April (30 days)
                Arguments.of("9010707080045", false)  // Invalid gender digit
        );
    }

    @ParameterizedTest
    @MethodSource("cnpProvider")
    void testCNPValidation(String cnp, boolean expectedValidation) {
        assertEquals(expectedValidation, cnpValidator.isValid(cnp, context));
    }
}