import java.util.Scanner;

// Expense Class
class Expense {
    int expenseId;
    String description;
    double amount;
    String category;
    String date;
    boolean isActive;
    
    Expense() {
        this.isActive = false;
    }
    
    Expense(int expenseId, String description, double amount, String category, String date) {
        this.expenseId = expenseId;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.isActive = true;
    }
    
    void display() {
        System.out.printf("%-4d | %-20s | Rs.%-8.2f | %-15s | %s\n", 
                         expenseId, description, amount, category, date);
    }
}

// Budget Class
class Budget {
    String category;
    double budgetAmount;
    double spentAmount;
    
    Budget() {
        this.budgetAmount = 0;
        this.spentAmount = 0;
    }
    
    Budget(String category, double budgetAmount) {
        this.category = category;
        this.budgetAmount = budgetAmount;
        this.spentAmount = 0;
    }
    
    void display() {
        double remaining = budgetAmount - spentAmount;
        double percentage = (spentAmount / budgetAmount) * 100;
        
        System.out.printf("%-15s | Rs.%-10.2f | Rs.%-10.2f | Rs.%-10.2f | %.1f%%\n",
                         category, budgetAmount, spentAmount, remaining, percentage);
    }
}

// Expense Tracker System
class ExpenseTrackerSystem {
    private Expense[] expenses;
    private Budget[] budgets;
    private int totalExpenses;
    private int totalBudgets;
    private int maxSize;
    
    ExpenseTrackerSystem(int maxSize) {
        this.maxSize = maxSize;
        expenses = new Expense[maxSize];
        budgets = new Budget[10];
        totalExpenses = 0;
        totalBudgets = 0;
        
        for (int i = 0; i < maxSize; i++) {
            expenses[i] = new Expense();
        }
        
        for (int i = 0; i < 10; i++) {
            budgets[i] = new Budget();
        }
    }
    
    // Add Expense
    void addExpense(String description, double amount, String category, String date) {
        if (totalExpenses >= maxSize) {
            System.out.println("Cannot add more expenses. Maximum limit reached");
            return;
        }
        
        if (amount <= 0) {
            System.out.println("Amount must be greater than 0");
            return;
        }
        
        int expenseId = totalExpenses + 1;
        expenses[totalExpenses] = new Expense(expenseId, description, amount, category, date);
        totalExpenses++;
        
        // Update budget spent amount
        for (int i = 0; i < totalBudgets; i++) {
            if (budgets[i].category.equalsIgnoreCase(category)) {
                budgets[i].spentAmount += amount;
                
                // Check if budget exceeded
                if (budgets[i].spentAmount > budgets[i].budgetAmount) {
                    System.out.println("\nWarning: Budget exceeded for " + category + " category!");
                    System.out.printf("Budget: Rs.%.2f, Spent: Rs.%.2f\n", 
                                    budgets[i].budgetAmount, budgets[i].spentAmount);
                }
                break;
            }
        }
        
        System.out.println("Expense added successfully");
        System.out.println("Expense ID: " + expenseId);
    }
    
    // Delete Expense
    void deleteExpense(int expenseId) {
        for (int i = 0; i < totalExpenses; i++) {
            if (expenses[i].isActive && expenses[i].expenseId == expenseId) {
                
                // Update budget spent amount
                for (int j = 0; j < totalBudgets; j++) {
                    if (budgets[j].category.equalsIgnoreCase(expenses[i].category)) {
                        budgets[j].spentAmount -= expenses[i].amount;
                        break;
                    }
                }
                
                expenses[i].isActive = false;
                System.out.println("Expense deleted successfully");
                return;
            }
        }
        System.out.println("Expense not found");
    }
    
    // Display All Expenses
    void displayAllExpenses() {
        int activeCount = 0;
        
        System.out.println("\nAll Expenses:");
        System.out.printf("%-4s | %-20s | %-12s | %-15s | %s\n", 
                         "ID", "Description", "Amount", "Category", "Date");
        System.out.println("------------------------------------------------------------------------");
        
        for (int i = 0; i < totalExpenses; i++) {
            if (expenses[i].isActive) {
                expenses[i].display();
                activeCount++;
            }
        }
        
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Total Expenses: " + activeCount);
    }
    
    // Display Expenses by Category
    void displayByCategory(String category) {
        boolean found = false;
        double totalAmount = 0;
        
        System.out.println("\nExpenses in " + category + " Category:");
        System.out.printf("%-4s | %-20s | %-12s | %s\n", 
                         "ID", "Description", "Amount", "Date");
        System.out.println("----------------------------------------------------");
        
        for (int i = 0; i < totalExpenses; i++) {
            if (expenses[i].isActive && expenses[i].category.equalsIgnoreCase(category)) {
                System.out.printf("%-4d | %-20s | Rs.%-8.2f | %s\n",
                                expenses[i].expenseId, expenses[i].description,
                                expenses[i].amount, expenses[i].date);
                totalAmount += expenses[i].amount;
                found = true;
            }
        }
        
        System.out.println("----------------------------------------------------");
        if (!found) {
            System.out.println("No expenses found in this category");
        } else {
            System.out.printf("Total: Rs.%.2f\n", totalAmount);
        }
    }
    
    // Display Expenses by Date
    void displayByDate(String date) {
        boolean found = false;
        double totalAmount = 0;
        
        System.out.println("\nExpenses on " + date + ":");
        System.out.printf("%-4s | %-20s | %-12s | %s\n", 
                         "ID", "Description", "Amount", "Category");
        System.out.println("----------------------------------------------------");
        
        for (int i = 0; i < totalExpenses; i++) {
            if (expenses[i].isActive && expenses[i].date.equals(date)) {
                System.out.printf("%-4d | %-20s | Rs.%-8.2f | %s\n",
                                expenses[i].expenseId, expenses[i].description,
                                expenses[i].amount, expenses[i].category);
                totalAmount += expenses[i].amount;
                found = true;
            }
        }
        
        System.out.println("----------------------------------------------------");
        if (!found) {
            System.out.println("No expenses found on this date");
        } else {
            System.out.printf("Total: Rs.%.2f\n", totalAmount);
        }
    }
    
    // Search Expense by ID
    void searchExpense(int expenseId) {
        for (int i = 0; i < totalExpenses; i++) {
            if (expenses[i].isActive && expenses[i].expenseId == expenseId) {
                System.out.println("\nExpense Found:");
                System.out.println("Expense ID  : " + expenses[i].expenseId);
                System.out.println("Description : " + expenses[i].description);
                System.out.printf("Amount      : Rs.%.2f\n", expenses[i].amount);
                System.out.println("Category    : " + expenses[i].category);
                System.out.println("Date        : " + expenses[i].date);
                return;
            }
        }
        System.out.println("Expense not found");
    }
    
    // Set Budget for Category
    void setBudget(String category, double amount) {
        if (amount <= 0) {
            System.out.println("Budget amount must be greater than 0");
            return;
        }
        
        // Check if budget already exists for this category
        for (int i = 0; i < totalBudgets; i++) {
            if (budgets[i].category.equalsIgnoreCase(category)) {
                budgets[i].budgetAmount = amount;
                System.out.println("Budget updated for " + category);
                return;
            }
        }
        
        // Add new budget
        if (totalBudgets >= 10) {
            System.out.println("Maximum budget categories reached");
            return;
        }
        
        budgets[totalBudgets] = new Budget(category, amount);
        
        // Calculate already spent amount in this category
        for (int i = 0; i < totalExpenses; i++) {
            if (expenses[i].isActive && expenses[i].category.equalsIgnoreCase(category)) {
                budgets[totalBudgets].spentAmount += expenses[i].amount;
            }
        }
        
        totalBudgets++;
        System.out.println("Budget set successfully for " + category);
    }
    
    // Display All Budgets
    void displayBudgets() {
        if (totalBudgets == 0) {
            System.out.println("\nNo budgets set yet");
            return;
        }
        
        System.out.println("\nBudget Summary:");
        System.out.printf("%-15s | %-12s | %-12s | %-12s | %s\n",
                         "Category", "Budget", "Spent", "Remaining", "Usage");
        System.out.println("------------------------------------------------------------------------");
        
        for (int i = 0; i < totalBudgets; i++) {
            budgets[i].display();
        }
        
        System.out.println("------------------------------------------------------------------------");
    }
    
    // Calculate Total Expenses
    double calculateTotalExpenses() {
        double total = 0;
        for (int i = 0; i < totalExpenses; i++) {
            if (expenses[i].isActive) {
                total += expenses[i].amount;
            }
        }
        return total;
    }
    
    // Calculate Category Wise Total
    void categoryWiseTotal() {
        String[] categories = new String[20];
        double[] totals = new double[20];
        int categoryCount = 0;
        
        for (int i = 0; i < totalExpenses; i++) {
            if (expenses[i].isActive) {
                boolean found = false;
                
                for (int j = 0; j < categoryCount; j++) {
                    if (categories[j].equalsIgnoreCase(expenses[i].category)) {
                        totals[j] += expenses[i].amount;
                        found = true;
                        break;
                    }
                }
                
                if (!found) {
                    categories[categoryCount] = expenses[i].category;
                    totals[categoryCount] = expenses[i].amount;
                    categoryCount++;
                }
            }
        }
        
        if (categoryCount == 0) {
            System.out.println("\nNo expenses to show");
            return;
        }
        
        System.out.println("\nCategory-wise Spending:");
        System.out.printf("%-15s | %-12s | %s\n", "Category", "Amount", "Percentage");
        System.out.println("---------------------------------------------");
        
        double grandTotal = calculateTotalExpenses();
        
        for (int i = 0; i < categoryCount; i++) {
            double percentage = (totals[i] / grandTotal) * 100;
            System.out.printf("%-15s | Rs.%-8.2f | %.1f%%\n", 
                            categories[i], totals[i], percentage);
        }
        
        System.out.println("---------------------------------------------");
        System.out.printf("Total: Rs.%.2f\n", grandTotal);
    }
    

}

// Main Class
public class ExpenseTracker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ExpenseTrackerSystem ets = new ExpenseTrackerSystem(200);
        
        // Add sample data
        System.out.println("Loading sample data...\n");
        ets.addExpense("Groceries", 1500.00, "Food", "01-12-2024");
        ets.addExpense("Movie tickets", 600.00, "Entertainment", "02-12-2024");
        ets.addExpense("Electricity bill", 800.00, "Bills", "03-12-2024");
        ets.addExpense("Lunch", 250.00, "Food", "04-12-2024");
        ets.addExpense("Petrol", 1200.00, "Transport", "05-12-2024");
        
        ets.setBudget("Food", 5000.00);
        ets.setBudget("Entertainment", 2000.00);
        ets.setBudget("Bills", 3000.00);
        ets.setBudget("Transport", 4000.00);
        
        int choice;
        
        while (true) {
            System.out.println("\n========================================");
            System.out.println("      EXPENSE TRACKER");
            System.out.println("========================================");
            System.out.println(" 1. Add Expense");
            System.out.println(" 2. Delete Expense");
            System.out.println(" 3. Display All Expenses");
            System.out.println(" 4. Search Expense by ID");
            System.out.println(" 5. Display by Category");
            System.out.println(" 6. Display by Date");
            System.out.println(" 7. Set Budget");
            System.out.println(" 8. Display Budgets");
            System.out.println(" 9. Category-wise Total");
            System.out.println(" 10. Exit");
            System.out.println("========================================");
            System.out.print("Enter your choice: ");
            
            choice = sc.nextInt();
            
            switch (choice) {
                case 1:
                    System.out.println("\nAdd New Expense");
                    sc.nextLine();
                    System.out.print("Enter Description: ");
                    String description = sc.nextLine();
                    System.out.print("Enter Amount: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter Category (Food/Transport/Bills/Entertainment/Shopping/Other): ");
                    String category = sc.nextLine();
                    System.out.print("Enter Date (DD-MM-YYYY): ");
                    String date = sc.nextLine();
                    ets.addExpense(description, amount, category, date);
                    break;
                    
                case 2:
                    System.out.println("\nDelete Expense");
                    System.out.print("Enter Expense ID: ");
                    int expenseId = sc.nextInt();
                    ets.deleteExpense(expenseId);
                    break;
                    
                case 3:
                    ets.displayAllExpenses();
                    break;
                    
                case 4:
                    System.out.println("\nSearch Expense");
                    System.out.print("Enter Expense ID: ");
                    expenseId = sc.nextInt();
                    ets.searchExpense(expenseId);
                    break;
                    
                case 5:
                    System.out.println("\nDisplay by Category");
                    sc.nextLine();
                    System.out.print("Enter Category: ");
                    category = sc.nextLine();
                    ets.displayByCategory(category);
                    break;
                    
                case 6:
                    System.out.println("\nDisplay by Date");
                    sc.nextLine();
                    System.out.print("Enter Date (DD-MM-YYYY): ");
                    date = sc.nextLine();
                    ets.displayByDate(date);
                    break;
                    
                case 7:
                    System.out.println("\nSet Budget");
                    sc.nextLine();
                    System.out.print("Enter Category: ");
                    category = sc.nextLine();
                    System.out.print("Enter Budget Amount: ");
                    amount = sc.nextDouble();
                    ets.setBudget(category, amount);
                    break;
                    
                case 8:
                    ets.displayBudgets();
                    break;
                    
                case 9:
                    ets.categoryWiseTotal();
                    break;
                    
                case 10:
                    System.out.println("\nThank you for using Expense Tracker!");
                    sc.close();
                    return;
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}