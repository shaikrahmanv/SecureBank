package com.banking;

import java.util.Scanner;

public class ExternalTransferTest {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.print("Enter sender user ID: ");
		int senderId = sc.nextInt();

		sc.nextLine();

		System.out.print("Enter receiver account number: ");
		String receiverAccountNumber = sc.nextLine();

		System.out.print("Enter transfer amount: ");
		double amount = sc.nextDouble();

		TransferDAO transferDAO = new TransferDAO();

		boolean result = transferDAO.externalBankTransfer(senderId, receiverAccountNumber, amount);

		if (result) {
			System.out.println("Money transferred to other bank successfully!");
		} else {
			System.out.println("External money transfer failed!");
		}

		sc.close();
	}

}
