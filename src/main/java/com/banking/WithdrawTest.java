package com.banking;

import java.util.Scanner;

public class WithdrawTest {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		System.out.print("Enter user ID: ");
		int userId = sc.nextInt();

		System.out.print("Enter withdrawal amount: ");
		double amount = sc.nextDouble();

		BankDAO bankDAO = new BankDAO();

		boolean result = bankDAO.withdraw(userId, amount);

		if (result) {
			System.out.println("Withdrawal successful!");
		} else {
			System.out.println("Withdrawal failed!");
		}

		sc.close();
	}

}
