package com.pluralsight;

import java.io.*;
import java.util.Scanner;

public class AccountingLedgerApp {
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        //creating a scanner class
        Scanner scanner = new Scanner(System.in);
        boolean execute = true;

        while (execute){
            //Home screen options
            System.out.println("Welcome To The Accounting Ledger!");
            System.out.println("Please select from the following:");
            System.out.println("   D - Add Deposit");
            System.out.println("   P - Make a Payment");
            System.out.println("   L - Display the Ledger");
            System.out.println("   X - Exit");

            //contingent on user's choice
            String choice = scanner.nextLine().toUpperCase().trim();

            //if/else breakdown of user's choices
            if (choice.equals("D")) {
                addDeposit(scanner);
            } else if (choice.equals("E")) {

        }
            if (choice.equals("P")) {
                makePayment(scanner);
            } else if (choice.equals("E")) {

            }
            if (choice.equals("L")) {
                displayLedger(scanner);
            } else if (choice.equals("E")) {

            }

        }

        }
            //Home Screen
            //D Add Deposit
            //P Make Payment
            //L Display Ledger
            //X Exit App
            public final static String dataFileName = "transactions.csv";
            public static ArrayList<LedgerFunctions> transactions = getTransaction();

            static void homeScreen(){
                do {
                    try {
                        System.out.println("Welcome To The Accounting Ledger!");
                        System.out.println("Please select from the following:");
                        System.out.println("   D - Add Deposit");
                        System.out.println("   P - Make a Payment");
                        System.out.println("   L - Display the Ledger");
                        System.out.println("   X - Exit");
                        System.out.print("Command: ");
                        int option = Console.PromptForInt();


                        //add deposit
                        Save the transaction to the CSV file
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))) {
                            writer.write(transaction); // Write the transaction to the file
                            System.out.println("Great! Your deposit has been added successfully."); // Confirm success
                        } catch (IOException e) {
                            // Handle any I/O exceptions that occur
                            System.out.println("Oh no! There was an error saving your transaction: " + e.getMessage());
                        }

                        //make a payment
                        Save the transaction to the CSV file
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))) {
                            writer.write(transaction); // Write the transaction to the file
                            System.out.println("Great! Your deposit has been added successfully."); // Confirm success
                        } catch (IOException e) {
                            // Handle any I/O exceptions that occur
                            System.out.println("Oh no! There was an error saving your transaction: " + e.getMessage());
                        }

                    }
}
