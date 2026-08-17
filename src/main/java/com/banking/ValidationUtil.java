package com.banking;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidationUtil {

    public static boolean isValidEmail(String email) {

        return email != null
                && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isValidPhone(String phone) {

        return phone != null
                && phone.matches("\\d{10}");
    }

    public static boolean isValidPassword(String password) {

        return password != null
                && password.matches(
                    "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$"
                );
    }
    public static boolean isValidDateOfBirth(String dateOfBirth) {

        try {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate.parse(dateOfBirth, formatter);

            return true;

        } catch (DateTimeParseException e) {

            return false;
        }
    }
}