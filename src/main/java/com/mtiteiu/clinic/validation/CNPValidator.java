package com.mtiteiu.clinic.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Class validator for validating authenticity of CNPs
 */
@Slf4j
public class CNPValidator implements ConstraintValidator<ValidCNP, String> {
    @Override
    public void initialize(ValidCNP constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String cnp, ConstraintValidatorContext constraintValidatorContext) {
        if (cnp == null) return true;
        // Verifică dacă CNP-ul are 13 caractere
        if (cnp.length() != 13) {
            log.info("CNP length not accurate");
            return false;
        }

        // Verifică dacă toate caracterele sunt cifre
        if (!cnp.matches("\\d+")) {
            log.info("CNP can only contain digits");
            return false;
        }

        // Verifică cifra de control
        int[] weights = {2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9};
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += Integer.parseInt(String.valueOf(cnp.charAt(i))) * weights[i];
        }
        int controlDigit = sum % 11;
        if (controlDigit == 10) {
            controlDigit = 1;
        }
        int lastDigit = Integer.parseInt(String.valueOf(cnp.charAt(12)));
        if (controlDigit != lastDigit) {
            return false;
        }

        // Verifică sexul și anul de naștere
        int genderDigit = Integer.parseInt(cnp.substring(0, 1));
        int year = Integer.parseInt(cnp.substring(1, 3));
        int month = Integer.parseInt(cnp.substring(3, 5));
        int day = Integer.parseInt(cnp.substring(5, 7));
        if (year < 0 || year > 99 || month < 1 || month > 12 || day < 1 || day > 31) {
            log.error("CNP invalid");
            return false;
        }

        // Verifică validitatea lunii pentru februarie
        boolean leapYear = ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0));
        if (month == 2 && (day > 28 && !leapYear)) {
            log.error("CNP invalid");
            return false;
        }

        // Verifică validitatea zilei în funcție de lună
        if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
            log.error("CNP invalid");
            return false;
        }

        // Verifică genul
        return genderDigit > 0 && genderDigit < 9;
    }
}
