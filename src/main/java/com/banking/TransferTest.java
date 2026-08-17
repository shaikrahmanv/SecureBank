package com.banking;

import java.util.Scanner;

public class TransferTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter sender user ID: ");
        int senderId = sc.nextInt();

        sc.nextLine();

        System.out.print("Enter receiver account number: ");
        String receiverAccountNumber = sc.nextLine();

        System.out.print("Enter transfer amount: ");
        double amount = sc.nextDouble();

        TransferDAO transferDAO = new TransferDAO();

        boolean result = transferDAO.sameBankTransfer(
                senderId,
                receiverAccountNumber,
                amount
        );

        if (result) {
            System.out.println("Money transferred successfully!");
        } else {
            System.out.println("Money transfer failed!");
        }

        sc.close();
    }
}
