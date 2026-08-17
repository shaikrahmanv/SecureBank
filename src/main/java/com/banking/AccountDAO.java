package com.banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDAO {

	public void showAccountDetails(int userId) {

		String sql = "SELECT name, contact_number, email, " + "account_type, balance, account_number "
				+ "FROM users WHERE id = ?";

		try {

			Connection connection = DBconnection.getConnection();

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, userId);

			ResultSet result = statement.executeQuery();

			if (result.next()) {

				System.out.println("\n================================");
				System.out.println("        ACCOUNT DETAILS");
				System.out.println("================================");

				System.out.println("Name           : " + result.getString("name"));

				System.out.println("User ID        : " + userId);

				System.out.println("Account Number : " + result.getString("account_number"));

				System.out.println("Account Type   : " + result.getString("account_type"));

				System.out.println("Email          : " + result.getString("email"));

				System.out.println("Contact Number : " + result.getString("contact_number"));

				System.out.println("Balance        : ₹" + result.getDouble("balance"));

				System.out.println("================================");

			} else {

				System.out.println("Account not found.");
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}