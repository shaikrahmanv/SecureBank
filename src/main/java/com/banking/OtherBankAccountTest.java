package com.banking;

public class OtherBankAccountTest {

	public static void main(String[] args) {

		OtherBankDAO otherBankDAO = new OtherBankDAO();

		boolean result = otherBankDAO.createAccount("2000000001", "Ali");

		if (result) {
			System.out.println("Other bank account created successfully!");
		} else {
			System.out.println("Account creation failed!");
		}
	}
}