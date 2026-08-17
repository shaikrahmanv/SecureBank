package com.banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO {

	public boolean registerUser(User user) {

		try {
			Connection connection = DBconnection.getConnection();

			if (connection == null) {
				System.out.println("Unable to connect to database.");
				return false;
			}
			// 1. Check whether email or contact number already exists
			String checkSql = "SELECT email, contact_number FROM users " + "WHERE email = ? OR contact_number = ?";

			PreparedStatement checkStatement = connection.prepareStatement(checkSql);

			checkStatement.setString(1, user.getEmail());
			checkStatement.setString(2, user.getContactNumber());

			ResultSet checkResult = checkStatement.executeQuery();

			if (checkResult.next()) {

				String existingEmail = checkResult.getString("email");
				String existingContact = checkResult.getString("contact_number");

				if (existingEmail.equalsIgnoreCase(user.getEmail())) {
					System.out.println("Email already registered!");
				}

				if (existingContact.equals(user.getContactNumber())) {
					System.out.println("Contact number already registered!");
				}

				return false;
			}

			// 2. Register the new user
			String sql = "INSERT INTO users " + "(name, contact_number, email, password, "
					+ "date_of_birth, address, account_type) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

			statement.setString(1, user.getName());
			statement.setString(2, user.getContactNumber());
			statement.setString(3, user.getEmail());
			String hashedPassword = PasswordUtil.hashPassword(user.getPassword());

			statement.setString(4, hashedPassword);
			statement.setString(5, user.getDateOfBirth());
			statement.setString(6, user.getAddress());
			statement.setString(7, user.getAccountType());

			statement.executeUpdate();

			// 3. Get automatically generated User ID
			ResultSet generatedKeys = statement.getGeneratedKeys();

			if (generatedKeys.next()) {

				int userId = generatedKeys.getInt(1);

				// 4. Generate account number automatically
				String accountNumber = String.valueOf(1000000000L + userId);
				user.setId(userId);
				user.setAccountNumber(accountNumber);

				String updateSql = "UPDATE users SET account_number = ? WHERE id = ?";

				PreparedStatement updateStatement = connection.prepareStatement(updateSql);

				updateStatement.setString(1, accountNumber);
				updateStatement.setInt(2, userId);

				updateStatement.executeUpdate();

				System.out.println("User registered successfully!");
				System.out.println("Your User ID: " + userId);
				System.out.println("Your Account Number: " + accountNumber);

				return true;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;
	}

	public boolean depositMoney(int userId, double amount, String method) {
		if (amount <= 0) {
			System.out.println("Invalid deposit amount!");
			return false;
		}
		try {

			Connection connection = DBconnection.getConnection();

			if (connection == null) {
				return false;
			}

			System.out.println("User ID received: " + userId);
			System.out.println("Amount received: " + amount);
			System.out.println("Method received: " + method);

			String sql = "UPDATE users SET balance = balance + ? WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setDouble(1, amount);
			statement.setInt(2, userId);

			int rowsUpdated = statement.executeUpdate();

			if (rowsUpdated > 0) {

				String transactionSql = "INSERT INTO transactions "
						+ "(user_id, transaction_type, amount, details, method) " + "VALUES (?, ?, ?, ?, ?)";

				PreparedStatement transactionStatement = connection.prepareStatement(transactionSql);

				transactionStatement.setInt(1, userId);
				transactionStatement.setString(2, "DEPOSIT");
				transactionStatement.setDouble(3, amount);
				transactionStatement.setString(4, "From: " + method);
				transactionStatement.setString(5, method);

				transactionStatement.executeUpdate();

				System.out.println("Deposit successful!");
				System.out.println("Transaction recorded!");

				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean withdrawMoney(int userId, double amount, String method) {
		if (amount <= 0) {
			System.out.println("Invalid withdrawal amount!");
			return false;
		}

		try {

			Connection connection = DBconnection.getConnection();

			if (connection == null) {
				return false;
			}

			String sql = "UPDATE users SET balance = balance - ? " + "WHERE id = ? AND balance >= ?";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setDouble(1, amount);
			statement.setInt(2, userId);
			statement.setDouble(3, amount);

			int rowsUpdated = statement.executeUpdate();

			if (rowsUpdated > 0) {

				String transactionSql = "INSERT INTO transactions "
						+ "(user_id, transaction_type, amount, details, method) " + "VALUES (?, ?, ?, ?, ?)";

				PreparedStatement transactionStatement = connection.prepareStatement(transactionSql);

				transactionStatement.setInt(1, userId);
				transactionStatement.setString(2, "WITHDRAW");
				transactionStatement.setDouble(3, amount);
				transactionStatement.setString(4, "Using: " + method);
				transactionStatement.setString(5, method);

				transactionStatement.executeUpdate();

				System.out.println("Withdrawal successful!");
				System.out.println("Transaction recorded!");

				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean transferMoney(int senderId, String receiverAccount, double amount) {
		if (amount <= 0) {
			System.out.println("Invalid transfer amount!");
			return false;
		}

		Connection connection = null;

		try {

			connection = DBconnection.getConnection();

			if (connection == null) {
				return false;
			}

			// Start database transaction
			connection.setAutoCommit(false);

			// 1. Find receiver using account number
			String receiverSql = "SELECT id, name FROM users WHERE account_number = ?";

			PreparedStatement receiverStatement = connection.prepareStatement(receiverSql);

			receiverStatement.setString(1, receiverAccount);

			ResultSet receiverResult = receiverStatement.executeQuery();

			if (!receiverResult.next()) {
				connection.rollback();
				return false;
			}

			int receiverId = receiverResult.getInt("id");
			String receiverName = receiverResult.getString("name");

			// Prevent transferring money to yourself
			if (senderId == receiverId) {
				connection.rollback();
				return false;
			}

			// 2. Deduct money from sender
			String senderSql = "UPDATE users SET balance = balance - ? " + "WHERE id = ? AND balance >= ?";

			PreparedStatement senderStatement = connection.prepareStatement(senderSql);

			senderStatement.setDouble(1, amount);
			senderStatement.setInt(2, senderId);
			senderStatement.setDouble(3, amount);

			int senderUpdated = senderStatement.executeUpdate();

			if (senderUpdated == 0) {
				connection.rollback();
				return false;
			}

			// 3. Add money to receiver
			String receiverUpdateSql = "UPDATE users SET balance = balance + ? WHERE id = ?";

			PreparedStatement receiverUpdateStatement = connection.prepareStatement(receiverUpdateSql);

			receiverUpdateStatement.setDouble(1, amount);
			receiverUpdateStatement.setInt(2, receiverId);

			int receiverUpdated = receiverUpdateStatement.executeUpdate();

			if (receiverUpdated == 0) {
				connection.rollback();
				return false;
			}

			// 4. Record transaction
			String transactionSql = "INSERT INTO transactions "
					+ "(user_id, transaction_type, amount, receiver_id, receiver_account, details, method) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement transactionStatement = connection.prepareStatement(transactionSql);

			transactionStatement.setInt(1, senderId);
			transactionStatement.setString(2, "TRANSFER");
			transactionStatement.setDouble(3, amount);
			transactionStatement.setInt(4, receiverId);
			transactionStatement.setString(5, receiverAccount);
			transactionStatement.setString(6, "To: " + receiverName);
			transactionStatement.setString(7, "Digital");

			transactionStatement.executeUpdate();
			// 5. Record receiver transaction

			String receiverTransactionSql = "INSERT INTO transactions "
					+ "(user_id, transaction_type, amount, receiver_id, receiver_account, details, method) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement receiverTransactionStatement = connection.prepareStatement(receiverTransactionSql);

			receiverTransactionStatement.setInt(1, receiverId);
			receiverTransactionStatement.setString(2, "TRANSFER");
			receiverTransactionStatement.setDouble(3, amount);
			receiverTransactionStatement.setInt(4, senderId);
			receiverTransactionStatement.setString(5, receiverAccount);
			receiverTransactionStatement.setString(6, "From: Sender");
			receiverTransactionStatement.setString(7, "Digital");

			receiverTransactionStatement.executeUpdate();

			// Everything succeeded
			connection.commit();

			System.out.println("Transfer successful!");
			System.out.println("Amount transferred: " + amount);
			System.out.println("Receiver account: " + receiverAccount);

			return true;

		} catch (Exception e) {

			e.printStackTrace();

			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (Exception rollbackException) {
				rollbackException.printStackTrace();
			}

		} finally {

			try {
				if (connection != null) {
					connection.setAutoCommit(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public List<Map<String, Object>> getTransactions(int userId) {

		List<Map<String, Object>> transactions = new ArrayList<>();

		try {

			Connection connection = DBconnection.getConnection();

			if (connection == null) {
				return transactions;
			}

			String sql = "SELECT transaction_type, amount, transaction_date, "
					+ "receiver_id, receiver_account, details, method " + "FROM transactions " + "WHERE user_id = ? "
					+ "ORDER BY transaction_date DESC";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, userId);

			ResultSet result = statement.executeQuery();

			while (result.next()) {

				Map<String, Object> transaction = new HashMap<>();

				transaction.put("type", result.getString("transaction_type"));
				transaction.put("amount", result.getDouble("amount"));
				transaction.put("date", result.getTimestamp("transaction_date"));
				transaction.put("receiverId", result.getObject("receiver_id"));
				transaction.put("receiverAccount", result.getString("receiver_account"));
				transaction.put("details", result.getString("details"));
				transaction.put("method", result.getString("method"));

				transactions.add(transaction);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transactions;
	}

	public User getUserById(int userId) {

		User user = null;

		try {

			Connection connection = DBconnection.getConnection();

			if (connection == null) {
				System.out.println("Database connection failed.");
				return null;
			}

			String sql = "SELECT * FROM users WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, userId);

			ResultSet result = statement.executeQuery();

			if (result.next()) {

				// Create User object
				user = new User(result.getString("name"), result.getString("contact_number"), result.getString("email"),
						result.getString("password"), result.getString("date_of_birth"), result.getString("address"),
						result.getString("account_type"));

				// User ID
				user.setId(result.getInt("id"));

				// Account Number
				user.setAccountNumber(result.getString("account_number"));

				// Balance
				user.setBalance(result.getDouble("balance"));

				// Console verification
				System.out.println("--------------------------------");
				System.out.println("Dashboard User ID: " + user.getId());

				System.out.println("Dashboard Account Number: " + user.getAccountNumber());

				System.out.println("Dashboard Name: " + user.getName());

				System.out.println("Dashboard Balance: " + user.getBalance());

				System.out.println("--------------------------------");
			}

			result.close();
			statement.close();
			connection.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return user;
	}
}
