package com.banking;

import java.sql.Connection;

public class TestConnection {

	public static void main(String[] args) {

		Connection connection = DBconnection.getConnection();

		if (connection != null) {
			System.out.println("Java connected to MySQL!");
		} else {
			System.out.println("Connection failed.");
		}

	}

}
