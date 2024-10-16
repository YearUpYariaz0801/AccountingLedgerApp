package com.pluralsight;
import java.io.*
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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

                   

                   }

               }
           }
        }

    //private static ArrayList<LedgerFunctions> getTransaction() {
    }


    //Ledger Screen
        //A Display All Entries
        //D Deposits Display all deposits into the account (positive entries)
        //P Display all payments made from the account (negative entries)
        //R Reports Navigates to Reports screen
        //H Return to the home page



        //Reports Screen
        //1) Month To Date
        System.out.println("Here are your Month to Date statements:");
        for (Map.Entry<String, Transaction> dateSet : transactionList.entrySet()) {
        String[] splitDates = dateSet.getValue().getDate().split("-");
        LocalDate today = LocalDate.now();
        int thisMonth = today.getMonthValue();
        int dayOfMonth = today.getDayOfMonth();
        if ((Integer.parseInt(splitDates[1]) == thisMonth) && (Integer.parseInt(splitDates[2]) <= dayOfMonth)) {
            System.out.println(dateSet.getValue().toString());
        }
        //2) Previous Month
        //System.out.println("Here are your Previous Month statements:");
        //for (Map.Entry<String, Transaction> dateSet : transactionList.entrySet()) {
            ///String[] splitDates = dateSet.getValue().getDate().split("-");
            //LocalDate today = LocalDate.now();
           // int thisMonth = today.getMonthValue();
            //int dayOfMonth = today.getDayOfMonth();
            //if ((Integer.parseInt(splitDates[1]) == thisMonth) && (Integer.parseInt(splitDates[2]) <= dayOfMonth)) {
                //System.out.println(dateSet.getValue().toString());
            }
        //3) Year To Date
        //System.out.println("Here are your Previous Month statements:");
        //for (Map.Entry<String, Transaction> dateSet : transactionList.entrySet()) {
        ///String[] splitDates = dateSet.getValue().getDate().split("-");
        //LocalDate today = LocalDate.now();
        // int thisMonth = today.getMonthValue();
        //int dayOfMonth = today.getDayOfMonth();
        //if ((Integer.parseInt(splitDates[1]) == thisMonth) && (Integer.parseInt(splitDates[2]) <= dayOfMonth)) {
        //System.out.println(dateSet.getValue().toString());
        }
        //4) Previous Year
        //System.out.println("Here are your Previous Month statements:");
        //for (Map.Entry<String, Transaction> dateSet : transactionList.entrySet()) {
        ///String[] splitDates = dateSet.getValue().getDate().split("-");
        //LocalDate today = LocalDate.now();
        // int thisMonth = today.getMonthValue();
        //int dayOfMonth = today.getDayOfMonth();
        //if ((Integer.parseInt(splitDates[1]) == thisMonth) && (Integer.parseInt(splitDates[2]) <= dayOfMonth)) {
        //System.out.println(dateSet.getValue().toString());
        }
        //5) Search By Vendor




        //Challenge: Custom Search
        //Start Date
        //End Date
        //Description
        //Vendor
        //Amount
        //Each filter should only call on and sort info based on the chosen filter and nothing else


        System.out.println();
    }
}