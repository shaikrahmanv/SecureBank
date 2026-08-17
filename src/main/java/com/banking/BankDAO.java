package com.banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BankDAO {

	public boolean deposit(int userId, double amount) {

		if (amount <= 0) {
			System.out.println("Deposit amount must be greater than 0.");
			return false;
		}

		String balanceSql = "UPDATE users SET balance = balance + ? WHERE id = ?";

		String transactionSql = "INSERT INTO transactions " + "(user_id, transaction_type, amount) "
				+ "VALUES (?, ?, ?)";

		Connection connection = null;

		try {

			connection = DBconnection.getConnection();

			if (connection == null) {
				System.out.println("Unable to connect to database.");
				return false;
			}

			connection.setAutoCommit(false);

			PreparedStatement balanceStatement = connection.prepareStatement(balanceSql);

			balanceStatement.setDouble(1, amount);
			balanceStatement.setInt(2, userId);

			int rows = balanceStatement.executeUpdate();

			if (rows == 0) {

				connection.rollback();
				System.out.println("User not found.");
				return false;
			}

			PreparedStatement transactionStatement = connection.prepareStatement(transactionSql);

			transactionStatement.setInt(1, userId);
			transactionStatement.setString(2, "DEPOSIT");
			transactionStatement.setDouble(3, amount);

			transactionStatement.executeUpdate();

			connection.commit();

			return true;

		} catch (Exception e) {

			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (Exception rollbackError) {
				rollbackError.printStackTrace();
			}

			e.printStackTrace();
			return false;
		}
	}

	public boolean withdraw(int userId, double amount) {

		if (amount <= 0) {
			System.out.println("Withdrawal amount must be greater than 0.");
			return false;
		}

		String balanceSql = "UPDATE users SET balance = balance - ? " + "WHERE id = ? AND balance >= ?";

		String transactionSql = "INSERT INTO transactions " + "(user_id, transaction_type, amount) "
				+ "VALUES (?, ?, ?)";

		Connection connection = null;

		try {

			connection = DBconnection.getConnection();

			if (connection == null) {
				System.out.println("Unable to connect to database.");
				return false;
			}

			connection.setAutoCommit(false);

			PreparedStatement balanceStatement = connection.prepareStatement(balanceSql);

			balanceStatement.setDouble(1, amount);
			balanceStatement.setInt(2, userId);
			balanceStatement.setDouble(3, amount);

			int rows = balanceStatement.executeUpdate();

			if (rows == 0) {

				connection.rollback();

				System.out.println("Insufficient balance.");
				return false;
			}

			PreparedStatement transactionStatement = connection.prepareStatement(transactionSql);

			transactionStatement.setInt(1, userId);
			transactionStatement.setString(2, "WITHDRAW");
			transactionStatement.setDouble(3, amount);

			transactionStatement.executeUpdate();

			connection.commit();

			return true;

		} catch (Exception e) {

			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (Exception rollbackError) {
				rollbackError.printStackTrace();
			}

			e.printStackTrace();
			return false;
		}
	}

	public double getBalance(int userId) {

		String sql = "SELECT balance FROM users WHERE id = ?";

		try {

			Connection connection = DBconnection.getConnection();

			if (connection == null) {
				System.out.println("Unable to connect to database.");
				return -1;
			}

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, userId);

			ResultSet result = statement.executeQuery();

			if (result.next()) {
				return result.getDouble("balance");
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return -1;
	}
}