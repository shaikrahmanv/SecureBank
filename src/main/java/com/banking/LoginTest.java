package com.banking;

import java.util.Scanner;

public class LoginTest {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.print("Enter email: ");
		String email = sc.nextLine();

		System.out.print("Enter password: ");
		String password = sc.nextLine();

		LoginDAO loginDAO = new LoginDAO();

		User user = loginDAO.loginUser(email, password);

		if (user != null) {
			System.out.println("Login successful!");
			System.out.println("Welcome " + user.getName());
		} else {
			System.out.println("Invalid email or password!");
		}

		sc.close();
	}
}
