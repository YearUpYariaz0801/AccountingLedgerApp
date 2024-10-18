package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

    public class Main {
        private static final String FILE_NAME = "Transactions.csv";
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //Connecting my homescreen to the main
        public static void main(String[] args) {
            while (true) {
                displayHomeScreen();
            }
        }

        //Displaying the Home Screen options
        private static void displayHomeScreen() {
            System.out.println("\nHome Screen:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("R) Reports");
            System.out.println("X) Exit");
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
                case "R":
                    displayReports();
                    break;
                case "X":
                    System.exit(0);
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }


        //Functional deposit prompt
        private static void addDeposit() {
            System.out.println("\nAdd Deposit:");
            String date = Console.PromptForString("Enter date (yyyy-MM-dd): ");
            String description = Console.PromptForString("Enter description: ");
            String vendor = Console.PromptForString("Enter vendor: ");
            double amount = Console.PromptForDouble("Enter amount: ");

            addEntryToLedger(date, description, vendor, amount);
            System.out.println("\nDeposit added.");
        }

        //Functional payment prompt
        private static void addPayment() {
            System.out.println("\nMake Payment:");
            String date = Console.PromptForString("Enter date (yyyy-MM-dd): ");
            String description = Console.PromptForString("Enter description: ");
            String vendor = Console.PromptForString("Enter vendor: ");
            double amount = Console.PromptForDouble("Enter amount (negative for payment): ");

            addEntryToLedger(date, description, vendor, amount);
            System.out.println("\nPayment added.");
        }

        //Add entry to ledger
        private static void addEntryToLedger(String date, String description, String vendor, double amount) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                writer.write(date + "," + description + "," + vendor + "," + amount);
                writer.newLine();
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        }

        //Ledger Screen
        private static void displayLedger() {
            System.out.println("\nLedger:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("(R) Reports");
            System.out.println("H) Home");
            String choice = Console.PromptForString("Choose an option: ").toUpperCase();

            switch (choice) {
                case "A":
                    displayLedger("ALL");
                    break;
                case "D":
                    displayLedger("DEPOSITS");
                    break;
                case "P":
                    displayLedger("PAYMENTS");
                    break;
                case "R":
                    displayLedger("REPORTS");
                    break;
                case "H":
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }


        //Ledger Functions
        private static void displayLedger(String filter) {
            List<String[]> ledgerEntries = readLedgerEntries();

            Collections.reverse(ledgerEntries); // Show newest entries first
            System.out.printf("%-15s %-15s %-15s %-10s\n", "Date", "Time", "Description", "Vendor", "Amount");

            for (String[] entry : ledgerEntries) {
                String date = entry[0];
                String time = entry[1];
                String description = entry[2];
                String vendor = entry[3];
                double amount = Double.parseDouble(entry[4]);

                switch (filter) {
                    case "DEPOSITS":
                        if (amount > 0) {
                            System.out.printf("%-15s | %-15s | %-15s | %-10.2f\n", date, time, description, vendor, amount);
                        }
                        break;
                    case "PAYMENTS":
                        if (amount < 0) {
                            System.out.printf("%-15s | %-15s | %-15s | %-10.2f\n", date, time, description, vendor, amount);
                        }
                        break;
                    case "ALL":
                    default:
                        System.out.printf("%-15s | %-15s | %-15s | %-10.2f\n", date, time, description, vendor, amount);
                }
            }
        }

        //Reads the csv file and returns the list of ledger entries
        private static List<String[]> readLedgerEntries() {
            List<String[]> entries = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    entries.add(line.split(","));
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
            return entries;
        }

        //Reports Menu Display
        private static void displayReports() {
            System.out.println("\nReports:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");
            String choice = Console.PromptForString("Choose an option: ");

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
        }

        //Function filters the entries to include only those within the specified date range.
        private static void displayDateRangeReport(LocalDate startDate, LocalDate endDate) {
            List<String[]> entries = readLedgerEntries();
            System.out.printf("%-15s | %-15s | %-15s | %-15s | %-10s\n", "Date", "Time", "Description", "Vendor", "Amount");

            for (String[] entry : entries) {
                LocalDate date = LocalDate.parse(entry[0], formatter);
                if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                    System.out.printf("%-15s %-15s %-15s %-10.2f\n", entry[0], entry[1], entry[2], Double.parseDouble(entry[3]));
                }
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

            List<String[]> entries = readLedgerEntries();
            System.out.printf("%-15s %-15s %-15s %-10s\n", "Date", "Description", "Vendor", "Amount");

            for (String[] entry : entries) {
                if (entry[2].toLowerCase().contains(vendorName)) {
                    System.out.printf("%-15s %-15s %-15s %-10.2f\n", entry[0], entry[1], entry[2], Double.parseDouble(entry[3]));
                }
            }
        }

        //Custom Search gives a variety of options
        private static void customSearch() {
            String startDateInput = Console.PromptForString("Start date (yyyy-MM-dd) or leave empty: ");
            String endDateInput = Console.PromptForString("End date (yyyy-MM-dd) or leave empty: ");
            String description = Console.PromptForString("Description or leave empty: ");
            String vendor = Console.PromptForString("Vendor or leave empty: ");
            String amountInput = Console.PromptForString("Amount or leave empty: ");

            LocalDate startDate = startDateInput.isEmpty() ? null : LocalDate.parse(startDateInput, formatter);
            LocalDate endDate = endDateInput.isEmpty() ? null : LocalDate.parse(endDateInput, formatter);
            Double amount = amountInput.isEmpty() ? null : Double.parseDouble(amountInput);

            List<String[]> entries = readLedgerEntries();
            System.out.printf("%-15s %-15s %-15s %-10s\n", "Date", "Description", "Vendor", "Amount");

            for (String[] entry : entries) {
                LocalDate date = LocalDate.parse(entry[0], formatter);
                String entryDescription = entry[1].toLowerCase();
                String entryVendor = entry[2].toLowerCase();
                double entryAmount = Double.parseDouble(entry[3]);

                if ((startDate == null || !date.isBefore(startDate)) &&
                        (endDate == null || !date.isAfter(endDate)) &&
                        (description.isEmpty() || entryDescription.contains(description.toLowerCase())) &&
                        (vendor.isEmpty() || entryVendor.contains(vendor.toLowerCase())) &&
                        (amount == null || entryAmount == amount)) {

                    System.out.printf("%-15s %-15s %-15s %-10.2f\n", entry[0], entry[1], entry[2], entryAmount);
                }
            }
        }
    }

