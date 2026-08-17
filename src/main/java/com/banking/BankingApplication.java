package com.banking;

import java.util.Scanner;

public class BankingApplication {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		System.out.println("================================");
		System.out.println("       WELCOME TO BANKING SYSTEM");
		System.out.println("================================");

		System.out.print("Enter email: ");
		String email = sc.nextLine();

		System.out.print("Enter password: ");
		String password = sc.nextLine();

		LoginDAO loginDAO = new LoginDAO();

		User user = loginDAO.loginUser(email, password);

		if (user != null) {

			System.out.println("\nLogin successful!");
			System.out.println("Welcome " + user.getName());
			System.out.println("Your User ID: " + user.getId());
			System.out.println("Your Account Type: " + user.getAccountType());

			while (true) {

				System.out.println("\n================================");
				System.out.println("          BANKING MENU");
				System.out.println("================================");
				System.out.println("1. Check Balance");
				System.out.println("2. Deposit");
				System.out.println("3. Withdraw");
				System.out.println("4. Transfer");
				System.out.println("5. Transaction History");
				System.out.println("6. Account Details");
				System.out.println("7. Logout");
				System.out.println("================================");

				System.out.print("Enter your choice: ");

				if (!sc.hasNextInt()) {
					System.out.println("Invalid input. Please enter a number.");
					sc.nextLine();
					continue;
				}

				int choice = sc.nextInt();

				// ==============================
				// 1. CHECK BALANCE
				// ==============================

				if (choice == 1) {

					BankDAO bankDAO = new BankDAO();

					double balance = bankDAO.getBalance(user.getId());

					if (balance >= 0) {
						System.out.println("Current balance: ₹" + balance);
					} else {
						System.out.println("Unable to retrieve balance.");
					}

				}

				// ==============================
				// 2. DEPOSIT
				// ==============================

				else if (choice == 2) {

					System.out.print("Enter deposit amount: ");

					if (!sc.hasNextDouble()) {
						System.out.println("Invalid amount. Please enter a number.");
						sc.nextLine();
						continue;
					}

					double amount = sc.nextDouble();

					BankDAO bankDAO = new BankDAO();

					boolean result = bankDAO.deposit(user.getId(), amount);

					if (result) {
						System.out.println("Deposit successful!");
					} else {
						System.out.println("Deposit failed!");
					}

				}

				// ==============================
				// 3. WITHDRAW
				// ==============================

				else if (choice == 3) {

					System.out.print("Enter withdrawal amount: ");

					if (!sc.hasNextDouble()) {
						System.out.println("Invalid amount. Please enter a number.");
						sc.nextLine();
						continue;
					}

					double amount = sc.nextDouble();

					BankDAO bankDAO = new BankDAO();

					boolean result = bankDAO.withdraw(user.getId(), amount);

					if (result) {
						System.out.println("Withdrawal successful!");
					} else {
						System.out.println("Withdrawal failed!");
					}

				}

				// ==============================
				// 4. TRANSFER
				// ==============================

				else if (choice == 4) {

					System.out.println("\n===== TRANSFER =====");
					System.out.println("1. Same Bank");
					System.out.println("2. Other Bank");

					System.out.print("Enter transfer type: ");

					if (!sc.hasNextInt()) {
						System.out.println("Invalid transfer type. Please enter 1 or 2.");
						sc.nextLine();
						continue;
					}

					int transferType = sc.nextInt();

					System.out.print("Enter receiver account number: ");
					String receiverAccountNumber = sc.next();

					if (!receiverAccountNumber.matches("\\d+")) {
						System.out.println("Invalid account number. Please enter digits only.");
						continue;
					}

					System.out.print("Enter transfer amount: ");

					if (!sc.hasNextDouble()) {
						System.out.println("Invalid amount. Please enter a number.");
						sc.nextLine();
						continue;
					}

					double amount = sc.nextDouble();

					TransferDAO transferDAO = new TransferDAO();

					boolean result;

					if (transferType == 1) {

						result = transferDAO.sameBankTransfer(user.getId(), receiverAccountNumber, amount);

					} else if (transferType == 2) {

						result = transferDAO.externalBankTransfer(user.getId(), receiverAccountNumber, amount);

					} else {

						System.out.println("Invalid transfer type.");
						continue;
					}

					if (result) {
						System.out.println("Money transferred successfully!");
					} else {
						System.out.println("Money transfer failed!");
					}

				}

				// ==============================
				// 5. TRANSACTION HISTORY
				// ==============================
				else if (choice == 5) {

					TransactionDAO transactionDAO = new TransactionDAO();

					transactionDAO.showTransactionHistory(user.getId());

				}

				else if (choice == 6) {

					AccountDAO accountDAO = new AccountDAO();

					accountDAO.showAccountDetails(user.getId());

				}

				else if (choice == 7) {

					System.out.println("Logged out successfully!");
					break;

				}

				else {

					System.out.println("Invalid choice.");

				}
			}

		} else {

			System.out.println("\nInvalid email or password!");
		}

		sc.close();
	}
}