# Changelog

### Changed
- Removed local Console class implementation in favor of external dependency
- Added yearup-utils dependency to pom.xml for console input handling
- 

### Removed
- Deleted Console.java class containing input handling methods:
```java:AccountingLedgerApp/src/main/java/com/pluralsight/Console.java
public class Console {
    static Scanner scanner = new Scanner(System.in);

    public static String PromptForString(String prompt){
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static boolean PromptForYesNo(String prompt){
        System.out.print(prompt + " ( Y for Yes, N for No ) ?");
        String userinput = scanner.nextLine();
        return (userinput.equalsIgnoreCase("Y") || userinput.equalsIgnoreCase("YES"));
    }

    // ... other prompt methods removed
}
```

### Added
- New Maven dependency:
```xml:AccountingLedgerApp/pom.xml
    <dependencies>
        <dependency>
            <groupId>com.abdurraheem</groupId>
            <artifactId>yearup-utils</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
```

### Refactor Transactions handling
- Created new Transactions class with static Transaction inner class
- Added file handling methods for loading and saving transactions:
```java:AccountingLedgerApp/src/main/java/com/pluralsight/Transactions.java
    public List<Transaction> loadTransactions(String Filename) throws IOException {
        List<Transaction> loadedTransactions = new ArrayList<>();
        FileReader fr = new FileReader(Filename);
        BufferedReader br = new BufferedReader(fr);

        try {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split("\\|");
                loadedTransactions.add(new Transaction(
                    lineSplit[0].trim(),
                    lineSplit[1].trim(),
                    lineSplit[2].trim(),
                    lineSplit[3].trim(),
                    Double.parseDouble(lineSplit[4].trim())
                ));
            }
        } finally {
            br.close();
        }
        return loadedTransactions;
    }
```

### Enhance data handling
- Added whitespace trimming for transaction fields
- Improved formatting in display methods:
```java:AccountingLedgerApp/src/main/java/com/pluralsight/Main.java
    System.out.printf("%15s | %15s | %30s | %20s | %15s \n", 
        "date", "time", "description", "vendor", "amount");

    for (Transactions.Transaction entry : ledgerEntries) {
        System.out.printf("%15s | %15s | %30s | %20s | %15.2f \n",
            entry.getDate(), 
            entry.getTime(), 
            entry.getDescription(), 
            entry.getVendor(), 
            entry.getAmount());
    }
```

### Additional Features
- Added "View Last Transaction" feature:
```java:AccountingLedgerApp/src/main/java/com/pluralsight/Main.java
    private static void displayLastTransaction() {
        try {
            List<Transactions.Transaction> transactions = transactionsHandler.loadTransactions(FILE_NAME);
            if (!transactions.isEmpty()) {
                Transactions.Transaction last = transactions.get(transactions.size() - 1);
                System.out.println("\nLast Transaction:");
                System.out.printf("Date: %s\nDescription: %s\nVendor: %s\nAmount: $%.2f\n",
                    last.getDate(), last.getDescription(), last.getVendor(), last.getAmount());
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
```

- Improved ledger display with balance and transaction counts:
```java:AccountingLedgerApp/src/main/java/com/pluralsight/Main.java
    double balance = 0.0;
    int deposits = 0;
    int payments = 0;
    
    for (Transactions.Transaction transaction : transactions) {
        double amount = transaction.getAmount();
        balance += amount;
        if (amount > 0) deposits++;
        else payments++;
    }
    
    System.out.printf("\nCurrent Balance: $%.2f\n", balance);
    System.out.printf("Total Transactions: %d (Deposits: %d, Payments: %d)\n", 
        deposits + payments, deposits, payments);
```

- Added "Today's Transactions" feature:
```java:AccountingLedgerApp/src/main/java/com/pluralsight/Main.java
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
```

- Enhanced home screen with transaction count display:
```java:AccountingLedgerApp/src/main/java/com/pluralsight/Main.java
System.out.println("\nWelcome to Your Accounting Ledger!");
System.out.println("-----------------------------------");
System.out.printf("Total Transactions: %d\n\n", transactions.size());
```

- Updated home screen menu with new options:
```java
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
``` 