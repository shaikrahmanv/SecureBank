package com.banking;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnection {
	  public static Connection getConnection() {

	        String url = "jdbc:mysql://localhost:3306/banking_db";
	        String username = System.getenv("DB_USERNAME");
	        String password = System.getenv("DB_PASSWORD");

	        try {
	            Connection connection = DriverManager.getConnection(url, username, password);
	            System.out.println("Database connected successfully!");
	            return connection;

	        } catch (Exception e) {
	            System.out.println("Database connection failed!");
	            e.printStackTrace();
	            return null;
	        }
	    }
}
