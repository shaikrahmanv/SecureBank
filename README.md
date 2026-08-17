# 🏦 SecureBank

A Java Spring Boot based banking web application that provides secure and user-friendly banking operations through a web interface.

## 🌐 Live Demo

The SecureBank application is deployed and available online.

🔗 **Live Application:**  
https://securebank-sr3n.onrender.com

---

## 📌 Project Overview

SecureBank is a full-stack banking application developed using **Java, Spring Boot, HTML, CSS, JavaScript, Thymeleaf, and MySQL**.

The application allows users to:

- Create a bank account
- Securely log in
- View account information
- Check account balance
- Deposit money
- Withdraw money
- Transfer funds
- Transfer money to external banks
- View transaction history
- Manage banking information

---

## ✨ Features

- 👤 User Registration
- 🔐 Secure Login
- 🏦 Account Management
- 💰 Balance Checking
- 💵 Deposit Money
- 🏧 Withdraw Money
- 🔄 Fund Transfer
- 🌍 External Bank Transfer Support
- 📋 Transaction History
- 👨‍💼 User Dashboard
- 🌍 Multiple Currency Support
- ✅ Input Validation
- 🔒 Session-Based Authentication
- 🔐 Database Credentials Protected with `.gitignore`
- 🐳 Docker Deployment
- ☁️ Render Deployment

---

## 🛠️ Technologies Used

### Backend

- Java
- Spring Boot
- JDBC
- MySQL

### Frontend

- HTML5
- CSS3
- JavaScript
- Thymeleaf

### Development Tools

- Eclipse
- Maven
- Git
- GitHub

### Deployment

- Docker
- Render

---

## 🗄️ Database

SecureBank uses **MySQL** for storing banking information such as:

- User account information
- Customer details
- Account numbers
- Account types
- Account currencies
- Account balances
- Transactions
- Banking details

Database credentials are kept outside the public repository using configuration files and `.gitignore`.

---

## 💱 Supported Currencies

The registration system supports multiple currencies, including:

- 🇮🇳 INR - Indian Rupee
- 🇺🇸 USD - US Dollar
- 🇪🇺 EUR - Euro
- 🇬🇧 GBP - British Pound
- 🇯🇵 JPY - Japanese Yen
- 🇨🇳 CNY - Chinese Yuan
- 🇰🇷 KRW - South Korean Won
- 🇷🇺 RUB - Russian Ruble
- 🇹🇷 TRY - Turkish Lira
- 🇻🇳 VND - Vietnamese Dong
- 🇳🇬 NGN - Nigerian Naira
- 🇦🇪 AED - UAE Dirham
- 🇸🇦 SAR - Saudi Riyal
- 🇨🇦 CAD - Canadian Dollar
- 🇦🇺 AUD - Australian Dollar
- 🇸🇬 SGD - Singapore Dollar
- 🇨🇭 CHF - Swiss Franc
- 🇧🇷 BRL - Brazilian Real
- 🇿🇦 ZAR - South African Rand

---

## 🔐 Authentication

Users can:

1. Register a new account.
2. Log in using their email and password.
3. Access their banking dashboard after successful authentication.
4. Access protected banking features through their session.
5. Log out from the banking system.

---

## 💳 Banking Operations

### 💰 Balance

Users can view their current account balance.

### 💵 Deposit

Users can deposit money into their account.

### 🏧 Withdraw

Users can withdraw available funds from their account.

### 🔄 Transfer

Users can transfer money to another account.

### 🌍 External Bank Transfer

The application also supports external bank transfer functionality.

### 📋 Transaction History

Users can view their previous banking transactions.

---

## 📂 Project Structure

```text
SecureBank/
│
├── src/
│   └── main/
│       ├── java/
│       │   ├── Jar/
│       │   │   ├── BankingWebApplication1Application.java
│       │   │   ├── HomeController.java
│       │   │   ├── LoginController.java
│       │   │   ├── BalanceController.java
│       │   │   └── ...
│       │   │
│       │   └── com/
│       │       └── banking/
│       │           ├── User.java
│       │           ├── UserDAO.java
│       │           ├── LoginDAO.java
│       │           └── ...
│       │
│       └── resources/
│           ├── templates/
│           │   ├── login.html
│           │   ├── register.html
│           │   ├── dashboard.html
│           │   ├── balance.html
│           │   └── ...
│           │
│           └── application.properties
│
├── Dockerfile
├── mvnw
├── pom.xml
├── .gitignore
└── README.md
