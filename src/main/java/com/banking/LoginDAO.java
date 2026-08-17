package com.banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAO {

	public User loginUser(String email, String password) {

		try {

			Connection connection = DBconnection.getConnection();

			if (connection == null) {
				System.out.println("Unable to connect to database.");
				return null;
			}

			String sql = "SELECT * FROM users WHERE email = ?";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, email);

			ResultSet result = statement.executeQuery();

			if (!result.next()) {
				System.out.println("Email not registered.");
				return null;
			}

			String storedPassword = result.getString("password");

			boolean passwordCorrect = false;

			// Check whether password is already hashed
			if (storedPassword.contains(":")) {

				passwordCorrect = PasswordUtil.verifyPassword(password, storedPassword);

			} else {

				// Support old plain-text passwords
				if (storedPassword.equals(password)) {

					passwordCorrect = true;

					// Upgrade old password to secure hash
					String hashedPassword = PasswordUtil.hashPassword(password);

					String updateSql = "UPDATE users SET password = ? WHERE id = ?";

					PreparedStatement updateStatement = connection.prepareStatement(updateSql);

					updateStatement.setString(1, hashedPassword);
					updateStatement.setInt(2, result.getInt("id"));

					updateStatement.executeUpdate();

					System.out.println("Password security upgraded.");
				}
			}

			if (!passwordCorrect) {
				System.out.println("Incorrect password.");
				return null;
			}

			// Create User object
			User user = new User(result.getString("name"), result.getString("contact_number"),
					result.getString("email"), result.getString("password"), result.getString("date_of_birth"),
					result.getString("address"), result.getString("account_type"));

			user.setId(result.getInt("id"));
			user.setBalance(result.getDouble("balance"));

			// Load currency from database
			String currency = result.getString("currency");

			if (currency != null && !currency.isEmpty()) {
				user.setCurrency(currency);
			} else {
				user.setCurrency("INR");
			}

			// Load account number
			user.setAccountNumber(result.getString("account_number"));

			return user;

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}
}