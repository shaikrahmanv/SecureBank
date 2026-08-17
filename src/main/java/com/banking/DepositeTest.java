package com.banking;

import java.util.Scanner;

public class DepositeTest {

	public static void main(String[] args) {
		
		        Scanner sc = new Scanner(System.in);

		        System.out.print("Enter user ID: ");
		        int userId = sc.nextInt();

		        System.out.print("Enter deposit amount: ");
		        double amount = sc.nextDouble();

		        BankDAO bankDAO = new BankDAO();

		        boolean result = bankDAO.deposit(userId, amount);

		        if (result) {
		            System.out.println("Deposit successful!");
		        } else {
		            System.out.println("Deposit failed!");
		        }

		        sc.close();
	}

}
