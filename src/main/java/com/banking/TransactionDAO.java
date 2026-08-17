package com.banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TransactionDAO {

	public void showTransactionHistory(int userId) {

		String sql = "SELECT transaction_type, amount, receiver_id, receiver_account "
				+ "FROM transactions WHERE user_id = ? " + "ORDER BY id DESC";

		try {

			Connection connection = DBconnection.getConnection();

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, userId);

			ResultSet result = statement.executeQuery();

			System.out.println("\n================================");
			System.out.println("       TRANSACTION HISTORY");
			System.out.println("================================");

			boolean found = false;

			while (result.next()) {

				found = true;

				String type = result.getString("transaction_type");
				double amount = result.getDouble("amount");

				System.out.println("Transaction: " + type);
				System.out.println("Amount: ₹" + amount);

				int receiverId = result.getInt("receiver_id");

				if (!result.wasNull()) {
					System.out.println("Receiver ID: " + receiverId);
				}

				String receiverAccount = result.getString("receiver_account");

				if (receiverAccount != null) {
					System.out.println("Receiver Account: " + receiverAccount);
				}

				System.out.println("--------------------------------");
			}

			if (!found) {
				System.out.println("No transactions found.");
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
