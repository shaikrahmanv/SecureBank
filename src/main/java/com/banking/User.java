package com.banking;

public class User {

	private int id;
	private String name;
	private String contactNumber;
	private String email;
	private String password;
	private String dateOfBirth;
	private String address;
	private String accountType;

	// Account number
	private String accountNumber;

	// Account balance
	private double balance;

	// Constructor
	public User(String name, String contactNumber, String email, String password, String dateOfBirth, String address,
			String accountType) {

		this.name = name;
		this.contactNumber = contactNumber;
		this.email = email;
		this.password = password;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.accountType = accountType;
	}

	// User ID
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// Name
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Contact Number
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	// Email
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// Password
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// Date of Birth
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	// Address
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// Account Type
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	// Account Number
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	// Balance
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}