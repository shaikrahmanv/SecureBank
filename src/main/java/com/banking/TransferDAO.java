package com.banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TransferDAO {

	public boolean sameBankTransfer(int senderId, String receiverAccountNumber, double amount) {

		if (amount <= 0) {
			System.out.println("Transfer amount must be greater than 0.");
			return false;
		}

		Connection connection = null;

		try {
			connection = DBconnection.getConnection();

			// Start database transaction
			connection.setAutoCommit(false);

			// 1. Find receiver
			String receiverSql = "SELECT id FROM users WHERE account_number = ?";

			PreparedStatement receiverStatement = connection.prepareStatement(receiverSql);

			receiverStatement.setString(1, receiverAccountNumber);

			ResultSet receiverResult = receiverStatement.executeQuery();

			if (!receiverResult.next()) {
				System.out.println("Receiver account not found.");
				connection.rollback();
				return false;
			}

			int receiverId = receiverResult.getInt("id");

			// Prevent self-transfer
			if (senderId == receiverId) {
				System.out.println("You cannot transfer money to yourself.");
				connection.rollback();
				return false;
			}

			// 2. Check sender balance
			String balanceSql = "SELECT balance FROM users WHERE id = ?";

			PreparedStatement balanceStatement = connection.prepareStatement(balanceSql);

			balanceStatement.setInt(1, senderId);

			ResultSet balanceResult = balanceStatement.executeQuery();

			if (!balanceResult.next()) {
				System.out.println("Sender account not found.");
				connection.rollback();
				return false;
			}

			double senderBalance = balanceResult.getDouble("balance");

			if (senderBalance < amount) {
				System.out.println("Insufficient balance.");
				connection.rollback();
				return false;
			}

			// 3. Deduct from sender
			String debitSql = "UPDATE users SET balance = balance - ? WHERE id = ?";

			PreparedStatement debitStatement = connection.prepareStatement(debitSql);

			debitStatement.setDouble(1, amount);
			debitStatement.setInt(2, senderId);

			debitStatement.executeUpdate();

			// 4. Credit receiver
			String creditSql = "UPDATE users SET balance = balance + ? WHERE id = ?";

			PreparedStatement creditStatement = connection.prepareStatement(creditSql);

			creditStatement.setDouble(1, amount);
			creditStatement.setInt(2, receiverId);

			creditStatement.executeUpdate();

			// 5. Record sender transaction
			String transactionSql = "INSERT INTO transactions " + "(user_id, transaction_type, amount, receiver_id) "
					+ "VALUES (?, ?, ?, ?)";

			PreparedStatement senderTransaction = connection.prepareStatement(transactionSql);

			senderTransaction.setInt(1, senderId);
			senderTransaction.setString(2, "TRANSFER_SENT");
			senderTransaction.setDouble(3, amount);
			senderTransaction.setInt(4, receiverId);

			senderTransaction.executeUpdate();

			// 6. Record receiver transaction
			PreparedStatement receiverTransaction = connection.prepareStatement(transactionSql);

			receiverTransaction.setInt(1, receiverId);
			receiverTransaction.setString(2, "TRANSFER_RECEIVED");
			receiverTransaction.setDouble(3, amount);
			receiverTransaction.setInt(4, senderId);

			receiverTransaction.executeUpdate();

			// Everything succeeded
			connection.commit();

			System.out.println("Same-bank transfer successful!");

			return true;

		} catch (Exception e) {

			if (connection != null) {
				try {
					connection.rollback();
					System.out.println("Transfer failed. Transaction rolled back.");
				} catch (Exception rollbackException) {
					rollbackException.printStackTrace();
				}
			}

			e.printStackTrace();
		}

		return false;
	}

	public boolean externalBankTransfer(int senderId, String receiverAccountNumber, double amount) {

		final double TRANSFER_FEE = 20.00;

		if (amount <= 0) {
			System.out.println("Transfer amount must be greater than 0.");
			return false;
		}

		double totalAmount = amount + TRANSFER_FEE;

		Connection connection = null;

		try {

			// 1. Check external account
			OtherBankDAO otherBankDAO = new OtherBankDAO();

			if (!otherBankDAO.accountExists(receiverAccountNumber)) {
				System.out.println("External bank account not found.");
				return false;
			}

			// 2. Connect to our bank
			connection = DBconnection.getConnection();
			connection.setAutoCommit(false);

			// 3. Check sender account
			String balanceSql = "SELECT balance FROM users WHERE id = ?";

			PreparedStatement balanceStatement = connection.prepareStatement(balanceSql);

			balanceStatement.setInt(1, senderId);

			ResultSet balanceResult = balanceStatement.executeQuery();

			if (!balanceResult.next()) {

				connection.rollback();

				System.out.println("Sender account not found.");
				return false;
			}

			double senderBalance = balanceResult.getDouble("balance");

			// 4. Check balance including fee
			if (senderBalance < totalAmount) {

				connection.rollback();

				System.out.println("Insufficient balance. " + "You need ₹" + totalAmount + " including ₹" + TRANSFER_FEE
						+ " transfer fee.");

				return false;
			}

			// 5. Debit sender
			String debitSql = "UPDATE users SET balance = balance - ? " + "WHERE id = ? AND balance >= ?";

			PreparedStatement debitStatement = connection.prepareStatement(debitSql);

			debitStatement.setDouble(1, totalAmount);
			debitStatement.setInt(2, senderId);
			debitStatement.setDouble(3, totalAmount);

			int rows = debitStatement.executeUpdate();

			if (rows == 0) {

				connection.rollback();

				System.out.println("Debit failed.");
				return false;
			}

			// 6. Credit external bank
			boolean credited = otherBankDAO.creditAccount(receiverAccountNumber, amount);

			if (!credited) {

				// Refund sender because external credit failed
				String refundSql = "UPDATE users SET balance = balance + ? " + "WHERE id = ?";

				PreparedStatement refundStatement = connection.prepareStatement(refundSql);

				refundStatement.setDouble(1, totalAmount);
				refundStatement.setInt(2, senderId);

				refundStatement.executeUpdate();

				connection.commit();

				System.out.println("External bank credit failed.");

				System.out.println("₹" + totalAmount + " has been refunded to your account.");

				return false;
			}

			// 7. Record external transfer
			String transactionSql = "INSERT INTO transactions "
					+ "(user_id, transaction_type, amount, receiver_account) " + "VALUES (?, ?, ?, ?)";

			PreparedStatement transactionStatement = connection.prepareStatement(transactionSql);

			transactionStatement.setInt(1, senderId);
			transactionStatement.setString(2, "EXTERNAL_TRANSFER");
			transactionStatement.setDouble(3, amount);
			transactionStatement.setString(4, receiverAccountNumber);

			transactionStatement.executeUpdate();

			// 8. Record transfer fee
			PreparedStatement feeStatement = connection.prepareStatement(transactionSql);

			feeStatement.setInt(1, senderId);
			feeStatement.setString(2, "TRANSFER_FEE");
			feeStatement.setDouble(3, TRANSFER_FEE);
			feeStatement.setString(4, receiverAccountNumber);

			feeStatement.executeUpdate();

			// 9. Everything succeeded
			connection.commit();

			System.out.println("External transfer successful!");
			System.out.println("Transfer amount: ₹" + amount);
			System.out.println("Transfer fee: ₹" + TRANSFER_FEE);
			System.out.println("Total deducted: ₹" + totalAmount);

			return true;

		} catch (Exception e) {

			if (connection != null) {

				try {

					connection.rollback();

					System.out.println("Transfer failed. " + "Transaction rolled back.");

				} catch (Exception rollbackException) {
					rollbackException.printStackTrace();
				}
			}

			e.printStackTrace();
		}

		return false;
	}

}