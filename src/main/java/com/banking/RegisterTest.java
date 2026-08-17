package com.banking;

import java.util.Scanner;

public class RegisterTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter name: ");
        String name = sc.nextLine();

        System.out.print("Enter contact number: ");
        String contactNumber = sc.nextLine();
        if (!ValidationUtil.isValidPhone(contactNumber)) {
            System.out.println("Invalid contact number. Enter exactly 10 digits.");
            sc.close();
            return;
        }

        System.out.print("Enter email: ");
        String email = sc.nextLine();
        if (!ValidationUtil.isValidEmail(email)) {
            System.out.println("Invalid email format.");
            sc.close();
            return;
        }

        System.out.print("Enter password: ");
        String password = sc.nextLine();
        if (!ValidationUtil.isValidPassword(password)) {

            System.out.println(
                "Invalid password. Password must contain at least 8 characters, "
                + "one uppercase letter, one lowercase letter, and one number."
            );

            sc.close();
            return;
        }

        // Date of Birth
        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dateOfBirth = sc.nextLine();
        if (!ValidationUtil.isValidDateOfBirth(dateOfBirth)) {

            System.out.println(
                "Invalid date of birth. Please use YYYY-MM-DD format."
            );

            sc.close();
            return;
        }

        // Address
        System.out.print("Enter address: ");
        String address = sc.nextLine();

        // Account Type Options
        System.out.println("\nSelect account type:");
        System.out.println("1. Savings");
        System.out.println("2. Current");
        System.out.print("Enter your choice: ");

        int accountChoice = sc.nextInt();

        String accountType;

        if (accountChoice == 1) {

            accountType = "Savings";

        } else if (accountChoice == 2) {

            accountType = "Current";

        } else {

            System.out.println("Invalid account type!");
            sc.close();
            return;
        }

        User user = new User(
                name,
                contactNumber,
                email,
                password,
                dateOfBirth,
                address,
                accountType
        );

        UserDAO userDAO = new UserDAO();

        boolean result = userDAO.registerUser(user);

        if (result) {
            System.out.println("Registration completed!");
        } else {
            System.out.println("Registration failed!");
        }

        sc.close();
    }
    
}