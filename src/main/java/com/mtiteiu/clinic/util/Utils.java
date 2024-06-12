package com.mtiteiu.clinic.util;

import java.time.LocalDate;

public class Utils {

    private Utils() {
    }

    public static LocalDate getBirthDateFromCNP(String cnp) {
        if (cnp == null || cnp.length() != 13) {
            throw new IllegalArgumentException("Invalid CNP length");
        }

        int year = Integer.parseInt(cnp.substring(1, 3));
        int month = Integer.parseInt(cnp.substring(3, 5));
        int day = Integer.parseInt(cnp.substring(5, 7));

        int century = Character.getNumericValue(cnp.charAt(0));
        if (century >= 5) {
            year += 2000;
        } else {
            year += 1900;
        }

        // Create and return LocalDate object
        return LocalDate.of(year, month, day);
    }
}
