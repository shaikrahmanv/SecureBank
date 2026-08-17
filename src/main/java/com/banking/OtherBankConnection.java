package com.banking;

import java.sql.Connection;
import java.sql.DriverManager;

public class OtherBankConnection {

	public static Connection getConnection() {

		String url = "jdbc:mysql://localhost:3306/other_bank_db";
		String username = "root";
		String password = "root";

		try {
			Connection connection = DriverManager.getConnection(url, username, password);

			System.out.println("Other bank database connected successfully!");

			return connection;

		} catch (Exception e) {
			System.out.println("Other bank database connection failed!");
			e.printStackTrace();
			return null;
		}
	}
}
