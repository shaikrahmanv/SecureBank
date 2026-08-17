package com.banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OtherBankDAO {

	public boolean accountExists(String accountNumber) {

		String sql = "SELECT account_number FROM accounts " + "WHERE account_number = ?";

		try {

			Connection connection = OtherBankConnection.getConnection();

			if (connection == null) {
			    System.out.println("Unable to connect to other bank database.");
			    return false;
			}

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, accountNumber);

			ResultSet result = statement.executeQuery();

			return result.next();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;
	}

	public boolean creditAccount(String accountNumber, double amount) {

		if (amount <= 0) {
			return false;
		}

		String sql = "UPDATE accounts SET balance = balance + ? " + "WHERE account_number = ?";

		Connection connection = null;

		try {

			connection = OtherBankConnection.getConnection();

			if (connection == null) {
			    System.out.println("Unable to connect to other bank database.");
			    return false;
			}

			connection.setAutoCommit(false);


			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setDouble(1, amount);
			statement.setString(2, accountNumber);

			int rows = statement.executeUpdate();

			if (rows == 0) {

				connection.rollback();
				return false;
			}

			connection.commit();

			return true;

		} catch (Exception e) {

			try {

				if (connection != null) {
					connection.rollback();
				}

			} catch (Exception rollbackException) {
				rollbackException.printStackTrace();
			}

			e.printStackTrace();
			return false;
		}
	}

	public boolean debitAccount(String accountNumber, double amount) {

		if (amount <= 0) {
			return false;
		}

		String sql = "UPDATE accounts SET balance = balance - ? " + "WHERE account_number = ? AND balance >= ?";

		Connection connection = null;

		try {

			connection = OtherBankConnection.getConnection();

			if (connection == null) {
			    System.out.println("Unable to connect to other bank database.");
			    return false;
			}

			connection.setAutoCommit(false);

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setDouble(1, amount);
			statement.setString(2, accountNumber);
			statement.setDouble(3, amount);

			int rows = statement.executeUpdate();

			if (rows == 0) {

				connection.rollback();
				return false;
			}

			connection.commit();

			return true;

		} catch (Exception e) {

			try {

				if (connection != null) {
					connection.rollback();
				}

			} catch (Exception rollbackException) {
				rollbackException.printStackTrace();
			}

			e.printStackTrace();
			return false;
		}
	}

	public boolean createAccount(String accountNumber, String customerName) {

		String sql = "INSERT INTO accounts " + "(account_number, customer_name, balance) " + "VALUES (?, ?, 0.00)";

		try {

			Connection connection = OtherBankConnection.getConnection();

			if (connection == null) {
			    System.out.println("Unable to connect to other bank database.");
			    return false;
			}

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, accountNumber);
			statement.setString(2, customerName);

			int rows = statement.executeUpdate();

			return rows > 0;

		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;
	}
}