package com.banking;

import java.sql.Connection;

public class OtherBankConnectionTest {

	public static void main(String[] args) {

		Connection connection = OtherBankConnection.getConnection();

		if (connection != null) {
			System.out.println("External bank connection test successful!");
		} else {
			System.out.println("External bank connection test failed!");
		}
	}
}