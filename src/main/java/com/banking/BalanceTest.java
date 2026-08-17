package com.banking;

import java.util.Scanner;

public class BalanceTest {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.print("Enter user ID: ");
		int userId = sc.nextInt();

		BankDAO bankDAO = new BankDAO();

		double balance = bankDAO.getBalance(userId);

		if (balance >= 0) {
			System.out.println("Current balance: ₹" + balance);
		} else {
			System.out.println("Unable to find account.");
		}

		sc.close();
	}

}
