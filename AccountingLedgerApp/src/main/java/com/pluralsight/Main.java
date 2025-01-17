package com.pluralsight;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Collections;
import com.abdurraheem.utils.Console;

public class Main {
    private static final String FILE_NAME = "AccountingLedgerApp/Transactions.csv";
    private static final LocalDateTime current = LocalDateTime.now();
    private static final DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter fmtTime = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static Transactions transactionsHandler = new Transactions();

    //Connecting my home screen to the main
    public static void main(String[] args) {
        while (true) {
            displayHomeScreen();
        }
    }

    //Displaying the Home Screen options
    private static void displayHomeScreen() {
        try {
            List<Transactions.Transaction> transactions = transactionsHandler.loadTransactions(FILE_NAME);
            System.out.println("\nWelcome to Your Accounting Ledger!");
            System.out.println("-----------------------------------");
            System.out.printf("Total Transactions: %d\n\n", transactions.size());

            //Enhancement
            String homeOptions = """
                    Please select from the following choices:
                    D - Add Deposit 
                    P - Make a Payment
                    L - View Ledger
                    V - View last transaction
                    T - Today's Transactions
                    R - View Reports
                    X - Exit
                    """;
            System.out.println(homeOptions);
            String choice = Console.PromptForString("Choose an option: ").toUpperCase();


            //Making the user options for the Home Screen
            switch (choice) {
                case "D":
                    addDeposit();
                    break;
                case "P":
                    addPayment();
                    break;
                case "L":
                    displayLedger();
                    break;
                case "V":
                    displayLastTransaction();
                    break;
                case "T":
                    displayTodayTransactions();
                    break;
                case "R":
                    displayReports();
                    break;
                case "X":
                    System.exit(0);
                default:
                    System.out.println("Invalid option, please try again.");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    //Functional debit prompt
    private static void addPayment() {
        //declare variables
        String date, time, description, vendor;
        double amount;
        boolean choice = Console.PromptForYesNo("Is this payment recent?");
        
        do {
            try {
                //if the user is adding a recent debit set date and time to current
                if (choice) {
                    date = current.format(fmtDate);
                    time = current.format(fmtTime);
                    description = Console.PromptForString(" Description: ");
                    vendor = Console.PromptForString(" Vendor: ");
                    amount = Console.PromptForDouble(" Amount: ");
                    System.out.println("                      $" + amount + " Debit pending...");
                    addEntryToLedger(date, time, description, vendor, amount);
                }

                //if deposit is adding a previous debit prompt for date and time
                if (!choice) {
                    date = Console.PromptForString(" Date (MM-dd-YYYY): ");
                    time = Console.PromptForString(" Time (HH:mm:ss): ");
                    description = Console.PromptForString(" Description: ");
                    vendor = Console.PromptForString(" Vendor: ");
                    amount = Console.PromptForDouble(" Amount : ");

                    System.out.println("                      $" + amount + " Debit pending...");
                    addEntryToLedger(date, time, description, vendor, amount);
                }
            } catch (Exception e) {
                System.out.println("\n ERROR! Debit can not be processed... ");
            }
        }
     while (Console.PromptForYesNo(" \nAdd another debit?")); //end loop when user input  no
    }

    //Functional deposit prompt
    private static void addDeposit() {

        //declare variables
        String date, time, description, vendor;
        double amount;

        do {
            //determine if a deposit is recent or old
            boolean choice = Console.PromptForYesNo("Is this deposit recent?");
            try {
                if (choice) {
                    date = current.format(fmtDate);
                    time = current.format(fmtTime);
                    description = Console.PromptForString(" Description: ");
                    vendor = Console.PromptForString(" Vendor: ");
                    amount = Console.PromptForDouble(" Amount: ");
                    addEntryToLedger(date, time, description, vendor, -amount); // Negative amount for payments
                    System.out.println("                     $" + amount + " deposit ending...");
                }

                if (!choice) {
                    date = Console.PromptForString(" Date (YYYY-MM-dd): ");
                    time = Console.PromptForString(" Time (HH:mm:ss): ");
                    description = Console.PromptForString(" Description: ");
                    vendor = Console.PromptForString(" Vendor: ");
                    amount = Console.PromptForDouble(" Amount: ");
                    addEntryToLedger(date, time, description, vendor, -amount); // Negative amount for payments
                    System.out.println("                     $" + amount + " deposit ending...");
                }
            } catch (Exception e) {
                System.out.println("\n ERROR! Deposit can not be process... ");
            }
        }
        while(Console.PromptForYesNo("\nAdd new deposit?")); //end loop when user inputs No

    }

    //Add entry to ledger using Transactions class
    private static void addEntryToLedger(String date, String time, String description, String vendor, double amount) {
        try {
            List<Transactions.Transaction> currentTransactions = transactionsHandler.loadTransactions(FILE_NAME);
            currentTransactions.add(new Transactions.Transaction(date, time, description, vendor, amount));
            transactionsHandler.saveTransactions(FILE_NAME, currentTransactions);
            System.out.println("Entry successfully added.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    //Ledger Screen
    private static void displayLedger() {
        try {
            List<Transactions.Transaction> transactions = transactionsHandler.loadTransactions(FILE_NAME);
            double balance = 0.0;
            int deposits = 0;
            int payments = 0;

            // Calculate totals
            for (Transactions.Transaction transaction : transactions) {
                double amount = transaction.getAmount();
                balance += amount;
                if (amount > 0) deposits++;
                else payments++;
            }

            // Display summary
            System.out.printf("\nCurrent Balance: $%.2f\n", balance);
            System.out.printf("Total Transactions: %d (Deposits: %d, Payments: %d)\n",
                    deposits + payments, deposits, payments);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        //enhanced with do and while loops
        while (true) {
            System.out.println("\nLedger:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("H) Home");
            String choice = Console.PromptForString("Choose an option: ").toUpperCase();

            do {
                try {
                    List<Transactions.Transaction> ledgerEntries = transactionsHandler.loadTransactions(FILE_NAME);
                    Collections.reverse(ledgerEntries); // Show newest entries first
                    System.out.printf("%15s | %15s | %30s | %20s | %15s \n", "date", "time", "description", "vendor", "amount");

                    for (Transactions.Transaction entry : ledgerEntries) {
                        switch (choice) {
                            case "A":
                                System.out.printf("%15s | %15s | %30s | %20s | %15.2f \n",
                                        entry.getDate(),
                                        entry.getTime(),
                                        entry.getDescription(),
                                        entry.getVendor(),
                                        entry.getAmount());
                                break;
                            case "D":
                                if (entry.getAmount() > 0) {
                                    System.out.printf("%15s | %15s | %30s | %20s | %15.2f \n",
                                            entry.getDate(),
                                            entry.getTime(),
                                            entry.getDescription(),
                                            entry.getVendor(),
                                            entry.getAmount());
                                }
                                break;
                            case "P":
                                if (entry.getAmount() < 0) {
                                    System.out.printf("%15s | %15s | %30s | %20s | %15.2f \n",
                                            entry.getDate(),
                                            entry.getTime(),
                                            entry.getDescription(),
                                            entry.getVendor(),
                                            entry.getAmount());
                                }
                                break;
                            case "H":
                                return;
                            default:
                                System.out.println("Invalid option, please try again.");
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error reading file: " + e.getMessage());
                }
            }
            while (!Console.PromptForYesNo("\nGo back?")); //end loop when user inputs Yes
        }
    }

    //Enhanced to stay in Report screen instead of returning  Home
    //Reports Menu Display
    private static void displayReports() {


        String reportOptions = """
                1- Month to Date
                2- Previous Month
                3- Year to Date
                4- Previous Year
                5- Search by Vendor 
                6- Custom Search 
                """;

        while (true) {
            System.out.println("\nReports:");
            System.out.println(reportOptions);
            String choice = Console.PromptForString("Choose an option: ");

            do {
                switch (choice) {
                    case "1":
                        displayDateRangeReport(LocalDate.now().withDayOfMonth(1), LocalDate.now());
                        break;
                    case "2":
                        displayPreviousMonthReport();
                        break;
                    case "3":
                        displayDateRangeReport(LocalDate.now().withDayOfYear(1), LocalDate.now());
                        break;
                    case "4":
                        displayPreviousYearReport();
                        break;
                    case "5":
                        searchByVendor();
                        break;
                    case "6":
                        customSearch();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Invalid option, please try again.");
                }
            } while (!Console.PromptForYesNo("\nGo back? ")); // return to report screen when user inputs Yes

        }
    }

    //Function filters the entries to include only those within the specified date range.
    private static void displayDateRangeReport(LocalDate startDate, LocalDate endDate) {
        try {
            List<Transactions.Transaction> entries = transactionsHandler.loadTransactions(FILE_NAME);
            System.out.printf("%15s | %15s | %30s | %20s | %15s \n", "date", "time", "description", "vendor", "amount");

            for (Transactions.Transaction entry : entries) {
                LocalDate date = LocalDate.parse(entry.getDate().trim(), fmtDate);
                if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                    System.out.printf("%15s | %15s | %30s | %20s | %15.2f \n",
                        entry.getDate(), 
                        entry.getTime(), 
                        entry.getDescription(), 
                        entry.getVendor(), 
                        entry.getAmount());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    //Function for Search for previous month's report
    private static void displayPreviousMonthReport() {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusMonths(1).withDayOfMonth(1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        displayDateRangeReport(startDate, endDate);
    }

    //Function for Search for previous year's report
    private static void displayPreviousYearReport() {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusYears(1).withDayOfYear(1);
        LocalDate endDate = now.minusYears(1).withDayOfYear(now.minusYears(1).lengthOfYear());
        displayDateRangeReport(startDate, endDate);
    }

    //Function for Search by vendor reports
    private static void searchByVendor() {
        String vendorName = Console.PromptForString("Enter vendor name: ").toLowerCase();

        try {
            List<Transactions.Transaction> entries = transactionsHandler.loadTransactions(FILE_NAME);
            System.out.printf("%15s | %15s | %30s | %20s | %15s \n", "date", "time", "description", "vendor", "amount");

            for (Transactions.Transaction entry : entries) {
                if (entry.getVendor().toLowerCase().contains(vendorName)) {
                    System.out.printf("%15s | %15s | %30s | %20s | %15.2f \n",
                        entry.getDate(), 
                        entry.getTime(), 
                        entry.getDescription(), 
                        entry.getVendor(), 
                        entry.getAmount());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    //Custom Search gives a variety of options
    private static void customSearch() {
        String startDateInput = Console.PromptForString("Start date (yyyy-MM-dd) or leave empty: ");
        String endDateInput = Console.PromptForString("End date (yyyy-MM-dd) or leave empty: ");
        String description = Console.PromptForString("Description or leave empty: ");
        String vendor = Console.PromptForString("Vendor or leave empty: ");
        String amountInput = Console.PromptForString("Amount or leave empty: ");

        LocalDate startDate = startDateInput.isEmpty() ? null : LocalDate.parse(startDateInput, fmtDate);
        LocalDate endDate = endDateInput.isEmpty() ? null : LocalDate.parse(endDateInput, fmtDate);
        Double amount = amountInput.isEmpty() ? null : Double.parseDouble(amountInput);

        try {
            List<Transactions.Transaction> entries = transactionsHandler.loadTransactions(FILE_NAME);
            System.out.printf("%15s | %15s | %30s | %20s | %15s \n", "date", "time", "description", "vendor", "amount");

            for (Transactions.Transaction entry : entries) {
                LocalDate date = LocalDate.parse(entry.getDate(), fmtDate);
                String entryDescription = entry.getDescription().toLowerCase();
                String entryVendor = entry.getVendor().toLowerCase();
                double entryAmount = entry.getAmount();

                if ((startDate == null || !date.isBefore(startDate)) &&
                    (endDate == null || !date.isAfter(endDate)) &&
                    (description.isEmpty() || entryDescription.contains(description.toLowerCase())) &&
                    (vendor.isEmpty() || entryVendor.contains(vendor.toLowerCase())) &&
                    (amount == null || entryAmount == amount)) {

                    System.out.printf("%15s | %15s | %30s | %20s | %15.2f \n",
                        entry.getDate(), 
                        entry.getTime(), 
                        entry.getDescription(), 
                        entry.getVendor(), 
                        entry.getAmount());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static void displayLastTransaction() {
        try {
            List<Transactions.Transaction> transactions = transactionsHandler.loadTransactions(FILE_NAME);
            if (!transactions.isEmpty()) {
                Transactions.Transaction last = transactions.get(transactions.size() - 1);
                System.out.println("\nLast Transaction:");
                System.out.printf("Date: %s\nDescription: %s\nVendor: %s\nAmount: $%.2f\n",
                    last.getDate(), last.getDescription(), last.getVendor(), last.getAmount());
            } else {
                System.out.println("No transactions found.");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static void displayTodayTransactions() {
        try {
            List<Transactions.Transaction> transactions = transactionsHandler.loadTransactions(FILE_NAME);
            String today = current.format(fmtDate);
            System.out.println("\nToday's Transactions:");
            
            boolean found = false;
            for (Transactions.Transaction t : transactions) {
                if (t.getDate().equals(today)) {
                    System.out.printf("%s | %s | %s | $%.2f\n",
                        t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    found = true;
                }
            }
            if (!found) System.out.println("No transactions today.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}