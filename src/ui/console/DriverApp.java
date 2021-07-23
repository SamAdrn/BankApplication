package ui.console;

import bank.Account;
import bank.Bank;
import bank.Branch;
import bank.Customer;
import business.BankManager;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class provides a console-based UI for users to perform operations.
 *
 * @author Samuel Kosasih
 */
public class DriverApp {

    /**
     * Business Layer. Manages the Banks in which the user has created.
     */
    private static final BankManager manager = new BankManager();
    /**
     * Scanner class. Used to retrieve input from user.
     */
    private static final Scanner scan = new Scanner(System.in);
    /**
     * Formats Strings to local currency.
     */
    private static final NumberFormat currency = NumberFormat.getCurrencyInstance();

    /**
     * Driver method. Runs the methods on the console.
     *
     * @param args command-line Strings
     */
    public static void main(String[] args) {
        printInstructions();
        int choice = 1;
        Bank selectedBank = null;
        Branch selectedBranch = null;
        Customer selectedCustomer = null;
        Account selectedAccount;
        boolean flag = true;
        while (flag) {
            switch (choice) {
                case 1:
                    selectedBank = selectBank();
                    if (selectedBank != null) {
                        choice = 2;
                    } else {
                        flag = false;
                    }
                    break;
                case 2:
                    selectedBranch = bankMenu(selectedBank);
                    if (selectedBranch != null) {
                        choice = 3;
                    } else {
                        choice = 1;
                    }
                    break;
                case 3:
                    selectedCustomer = branchMenu(selectedBranch, selectedBank);
                    if (selectedCustomer != null) {
                        choice = 4;
                    } else {
                        choice = 2;
                    }
                    break;
                case 4:
                    selectedAccount = customerMenu(selectedCustomer, selectedBranch);
                    if (selectedAccount != null) {
                        if (manageAccount(selectedAccount, selectedCustomer, selectedBranch) == -1) {
                            break;
                        }
                    } else {
                        choice = 3;
                    }
                    break;
                default:
                    flag = false;
                    break;
            }
        }
        if (manager.save()) {
            System.out.println("Database saved");
        } else {
            System.out.println("Saving error. This session is not saved.");
        }
        System.out.println("Thank you for using Banking Manager");
    }

    /**
     * Provides an overview of the program to the console.
     */
    private static void printInstructions() {
        System.out.println(
                "================================================================================\n" +
                        "Welcome to Banking Manager. Here, organize every inch of detail of your banks,\n" +
                        "from various branches to individual customer accounts. To begin, type in the \n" +
                        "appropriate input whenever prompted. To move back, simply enter -1. Enjoy.\n" +
                        "Made by: Samuel Kosasih\n" +
                        "================================================================================"
        );
    }

    /**
     * Allows the user to select Banks, create one, or remove it from the database.
     *
     * @return the selected Bank object
     */
    private static Bank selectBank() {
        while (true) {
            int choice;
            System.out.println("\nChoose a Bank (Enter the Bank ID): ");
            if (manager.getNumberOfBanks() == 0) {
                System.out.println("\t- No Banks Created -\n" +
                        "\tEnter 0 to create one.\n" +
                        "\tEnter -1 to quit");
            } else {
                for (Bank branches : manager) {
                    System.out.println("\t" + branches.simplifiedString());
                }
                System.out.println("\tEnter 0 to create one.\n" +
                        "\tEnter -1 to quit");
            }
            System.out.print("Selection: ");
            try {
                choice = scan.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("\nInvalid Input. Please enter integers only.");
                scan.nextLine();
                continue;
            }
            scan.nextLine();
            if (choice == 0) {
                System.out.println("\nYou have chosen to create a Bank. Enter \"0\" to cancel.");
                while (true) {
                    System.out.print("Please enter name of Bank: ");
                    String bankName = scan.nextLine().trim();
                    if (bankName.equals("0")) {
                        System.out.println("Bank not created.");
                        break;
                    }
                    if (isValidString(bankName)) {
                        manager.createBank(bankName);
                        System.out.println("\nBank is successfully created.");
                        break;
                    } else {
                        System.out.println("Bank name is invalid, please enter alphabets only.");
                    }
                }
            } else if (choice == -1) {
                return null;
            } else {
                Bank b = manager.getBank(choice);
                if (b != null) {
                    return b;
                } else {
                    System.out.println("\nUnrecognized Bank ID. Please retry selection.");
                }
            }
        }
    }

    /**
     * Allows the user to perform operations within a Bank, whether that be selecting
     * a Branch of the Bank, creating a Branch, or removing a Branch.
     *
     * @param bank the selected Bank
     * @return the selected Branch
     */
    private static Branch bankMenu(Bank bank) {
        while (true) {
            int choice;
            System.out.println("\nVisit Branch (Enter the Branch Code): \n" +
                    "\t" + bank.toString() + "\n" +
                    "\tEnter 0 to create one.\n" +
                    "\tEnter 1 to remove this bank from database.\n" +
                    "\tEnter -1 to reselect a bank."
            );
            System.out.print("Selection: ");
            try {
                choice = scan.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("\nInvalid Input. Please enter integers only.");
                scan.nextLine();
                continue;
            }
            scan.nextLine();

            if (choice == 0) {
                System.out.println("\nCreate a new branch. Enter \"0\" to cancel.");
                while (true) {
                    System.out.print("Please enter name of Branch: ");
                    String branchName = scan.nextLine().trim();
                    if (branchName.equals("0")) {
                        System.out.println("Branch not created.");
                        break;
                    }
                    if (isValidString(branchName)) {
                        System.out.println("Please enter Branch address (Street, City, State, Zip Code). " +
                                "Enter \"0\" to cancel.");
                        System.out.print("Address: ");
                        String branchAddress = scan.nextLine().trim();
                        if (branchAddress.equals("0")) {
                            System.out.println("Branch not created.");
                            break;
                        }
                        if (bank.createBranch(branchName, branchAddress)) {
                            System.out.println("\nBranch is successfully created.");
                            break;
                        } else {
                            System.out.println("Duplicate name of bank found. Please enter a distinguishable name.");
                        }
                    } else {
                        System.out.println("Branch name is invalid, please enter alphabets only.");
                    }
                }
            } else if (choice == -1) {
                return null;
            } else if (choice == 1) {
                System.out.println("\nAre you sure you want to remove " + bank.simplifiedString() +
                        " from the application database (once deleted, all data will be lost)?");
                System.out.println("Type \"YES\" to proceed.");
                System.out.print("Input: ");
                if (scan.nextLine().equalsIgnoreCase("yes")) {
                    String s = bank.simplifiedString();
                    if (manager.removeBank(bank.getBankId())) {
                        System.out.println(s + " has been removed.");
                        return null;
                    } else {
                        System.out.println("Removing error. Please contact developer.");
                    }
                }
            } else {
                Branch b = bank.getBranch(choice);
                if (b != null) {
                    return b;
                } else {
                    System.out.println("\nUnrecognized Branch code. Please retry selection.");
                }
            }
        }
    }

    /**
     * Allows the user to access Customer objects stored within the selected
     * Branch, or create/delete them from the database.
     *
     * @param branch the selected Branch
     * @param bank   the selected Bank
     * @return the selected Customer
     */
    private static Customer branchMenu(Branch branch, Bank bank) {
        while (true) {
            int choice;
            System.out.println("\nWelcome to the " + branch.getBranchName() + " branch.\n" +
                    "Branch details:\n" +
                    branch + "\n" +
                    "Enter customer ID to select a customer.\n" +
                    "Enter 0 to add a new customer.\n" +
                    "Enter 1 to remove branch.\n" +
                    "Enter -1 to reselect branch"
            );
            System.out.print("Selection: ");
            try {
                choice = scan.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("\nInvalid Input. Please enter integers only.");
                scan.nextLine();
                continue;
            }
            scan.nextLine();
            if (choice == 0) {
                System.out.println("\nAdd a new customer. Enter \"0\" to cancel.");
                boolean flag = true;
                while (flag) {
                    System.out.print("Please enter customer's full name: ");
                    String customerName = scan.nextLine();
                    if (customerName.equals("0")) {
                        System.out.println("Customer not added");
                        break;
                    }
                    if (isValidString(customerName)) {
                        System.out.println("Please enter customer's address (Street, City, State, Zip Code). " +
                                "Enter \"0\" to cancel.");
                        System.out.print("Address: ");
                        String customerAddress = scan.nextLine().trim();
                        if (customerAddress.equals("0")) {
                            System.out.println("Customer not added");
                            break;
                        }
                        if (branch.addCustomer(customerName, customerAddress)) {
                            System.out.println(customerName + " added.");
                        } else {
                            System.out.println("Could not add " + customerName + ". Possibility of duplicate.");
                        }
                        flag = false;
                    } else {
                        System.out.println("Customer name is invalid, please enter alphabets only.");
                    }
                }
            } else if (choice == -1) {
                return null;
            } else if (choice == 1) {
                System.out.println("\nAre you sure you want to remove " + branch.simplifiedString() +
                        " from the application database (once deleted, all data will be lost)?");
                System.out.println("Type \"YES\" to proceed.");
                System.out.print("Input: ");
                if (scan.nextLine().equalsIgnoreCase("yes")) {
                    String s = branch.simplifiedString();
                    if (bank.removeBranch(branch.getBranchCode())) {
                        System.out.println(s + " has been removed.");
                        return null;
                    } else {
                        System.out.println("Removing error. Please contact developer.");
                    }
                }
            } else {
                Customer c = branch.getCustomer(choice);
                if (c != null) {
                    return c;
                } else {
                    System.out.println("\nUnrecognized customer ID. Please retry selection.");
                }
            }
        }
    }

    /**
     * Allows the user to access Customer Accounts.
     *
     * @param customer the selected Customer
     * @param branch   the selected Branch
     * @return the selected Account
     */
    private static Account customerMenu(Customer customer, Branch branch) {
        while (true) {
            int choice;
            System.out.println("\nHello " + customer.getName() + "!\n" +
                    "Your details:\n" +
                    customer + "\n" +
                    "Enter Account Number to to manage funds.\n" +
                    "Enter 0 to open a new account.\n" +
                    "Enter 1 to close ALL accounts.\n" +
                    "Enter -1 to reselect customer"
            );
            System.out.print("Selection: ");
            try {
                choice = scan.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("\nInvalid Input. Please enter integers only.");
                scan.nextLine();
                continue;
            }
            scan.nextLine();
            if (choice == 0) {
                Account a = customer.openAccount();
                if (a != null) {
                    System.out.println("Congratulations! You have created a new account with us!");
                    System.out.println("\t" + a);
                } else {
                    System.out.println("It seems you have the maximum number of accounts open.");
                }
            } else if (choice == -1) {
                return null;
            } else if (choice == 1) {
                System.out.println("\nAre you sure you want to close all your accounts in "
                        + branch.simplifiedString() + " branch? We will be sorry to see you go.");
                System.out.println("Type \"YES\" to proceed.");
                System.out.print("Input: ");
                if (scan.nextLine().equalsIgnoreCase("yes")) {
                    String s = customer.simplifiedString();
                    if (branch.removeCustomer(customer.getCustomerId())) {
                        System.out.println(s + " is no longer a customer of " + branch.simplifiedString() + ".");
                        return null;
                    } else {
                        System.out.println("Removing error. Please contact developer.");
                    }
                }
            } else {
                Account a = customer.getAccount(choice);
                if (a != null) {
                    return a;
                } else {
                    System.out.println("\nUnrecognized account number. Please retry selection.");
                }
            }
        }
    }

    /**
     * Allows the user to manage a specific Account, from depositing and withdrawing money,
     * transferring money to another Customer within the Branch, to closing the Account itself.
     *
     * @param account  the selected Account
     * @param customer the selected Customer
     * @param branch   the selected Branch
     * @return returns -1, to indicate that the user is done managing the Account
     */
    private static int manageAccount(Account account, Customer customer, Branch branch) {
        while (true) {
            int choice;
            System.out.println("\nManaging " + account + "\n" +
                    "Enter 0 to deposit money into account.\n" +
                    "Enter 1 to withdraw money from account.\n" +
                    "Enter 2 to transfer money within " + branch.simplifiedString() + ".\n" +
                    "Enter 3 to close account.\n" +
                    "Enter -1 to reselect account"
            );
            System.out.print("Selection: ");
            try {
                choice = scan.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("\nInvalid Input. Please enter integers only.");
                scan.nextLine();
                continue;
            }
            scan.nextLine();
            if (choice == -1) {
                return choice;
            } else if (choice == 0) {
                System.out.println("Depositing into account " + account.getAccountNumber() + "\nEnter 0 to cancel" +
                        "\nHow much money would you like to deposit?");
                while (true) {
                    System.out.print("Amount: $");
                    try {
                        double amount = scan.nextDouble();
                        if (amount < 0) {
                            throw new InputMismatchException();
                        } else if (amount == 0) {
                            System.out.println("Deposit cancelled");
                            break;
                        } else {
                            account.deposit(amount);
                            System.out.println("Deposited " + currency.format(amount) + " into " +
                                    account.getAccountNumber() + ".");
                            break;
                        }
                    } catch (InputMismatchException ime) {
                        System.out.println("Invalid amount. Please enter a positive nominal.");
                        scan.nextLine();
                    }
                }
            } else if (choice == 1) {
                System.out.println("Withdrawing from account " + account.getAccountNumber() + ". Enter 0 to cancel" +
                        "\nBalance: " + currency.format(account.getBalance()) +
                        "\nHow much money would you like to withdraw?");
                while (true) {
                    System.out.print("Amount: $");
                    try {
                        double amount = scan.nextDouble();
                        if (amount < 0) {
                            throw new InputMismatchException();
                        } else if (amount == 0) {
                            System.out.println("Withdrawal cancelled");
                            break;
                        } else {
                            if (account.withdraw(amount)) {
                                System.out.println("Withdrew " + currency.format(amount) + " from " +
                                        account.getAccountNumber() + ".");
                                break;
                            } else {
                                System.out.println("Insufficient funds.\nBalance: " +
                                        currency.format(account.getBalance()));
                            }
                        }
                    } catch (InputMismatchException ime) {
                        System.out.println("Invalid amount. Please enter a positive nominal.");
                        scan.nextLine();
                    }
                }
            } else if (choice == 2) {
                System.out.println("Transferring funds from account " + account.getAccountNumber() +
                        ". Enter 0 to cancel" + "\nBalance: " + currency.format(account.getBalance()) +
                        "\n" + branch + "\nEnter recipient customer ID.");
                Customer recipient = null;
                while (true) {
                    System.out.print("Selection: ");
                    try {
                        choice = scan.nextInt();
                    } catch (InputMismatchException ime) {
                        System.out.println("\nInvalid Input. Please enter integers only.");
                        scan.nextLine();
                    }
                    if (choice == 0) {
                        break;
                    }
                    recipient = branch.getCustomer(choice);
                    if (recipient != null) {
                        if (recipient.getNumberOfAccounts() == 0) {
                            System.out.println("This customer has no accounts open.");
                        } else {
                            break;
                        }
                    } else {
                        System.out.println("\nUnrecognized customer ID. Please retry selection.");
                    }
                }
                if (recipient == null) {
                    break;
                }
                Account recipientAccount = null;
                System.out.println("\n" + recipient);
                System.out.println("Choose an account from the recipient's list. Enter 0 to quit.");
                while (true) {
                    System.out.print("Selection: ");
                    try {
                        choice = scan.nextInt();
                    } catch (InputMismatchException ime) {
                        System.out.println("\nInvalid Input. Please enter integers only.");
                        scan.nextLine();
                        continue;
                    }
                    if (choice == 0) {
                        break;
                    }
                    recipientAccount = recipient.getAccount(choice);
                    if (recipientAccount == null) {
                        System.out.println("\nUnrecognized account number. Please retry selection.");
                    } else {
                        break;
                    }
                }
                if (recipientAccount == null) {
                    break;
                }
                System.out.println("Transferring to " + recipient.simplifiedString() +
                        " (" + recipientAccount.getAccountNumber() + "). Enter 0 to cancel" +
                        "\nBalance: " + currency.format(recipientAccount.getBalance()) +
                        "\nHow much money would you like to transfer?");
                while (true) {
                    System.out.print("Amount: $");
                    try {
                        double amount = scan.nextDouble();
                        if (amount < 0) {
                            throw new InputMismatchException();
                        } else if (amount == 0) {
                            System.out.println("Transfer cancelled");
                            break;
                        } else {
                            if (account.transfer(recipientAccount, amount)) {
                                System.out.println("Transferred " + currency.format(amount) + " from " +
                                        account.getAccountNumber() + "" +
                                        " to " + recipient.simplifiedString() +
                                        " (" + recipientAccount.getAccountNumber() + ").");
                                break;
                            } else {
                                System.out.println("Insufficient funds.\nBalance: " +
                                        currency.format(account.getBalance()) + "\n");
                            }
                        }
                    } catch (InputMismatchException ime) {
                        System.out.println("Invalid amount. Please enter a positive nominal.");
                        scan.nextLine();
                    }
                }
            } else if (choice == 3) {
                System.out.println("\nAre you sure you want to close this account " +
                        "(ensure you have withdrawn all your funds)?");
                System.out.println("Type \"YES\" to proceed.");
                System.out.print("Input: ");
                if (scan.nextLine().equalsIgnoreCase("yes")) {
                    if (account.getBalance() != 0) {
                        System.out.println("Please empty funds before continuing\nBalance: " +
                                currency.format(account.getBalance()));
                        break;
                    }
                    String s = account.getAccountNumber() + " has been removed.";
                    if (customer.closeAccount(account)) {
                        System.out.println(s + " has been removed.");
                        return -1;
                    } else {
                        System.out.println("Removing error. Please contact developer.");
                    }
                }
            }
        }
        return -1;
    }

    /**
     * A helper method to indicate that a String is valid with alphabetical letters only.
     *
     * @param s the String to be checked
     * @return true if String is valid
     */
    private static boolean isValidString(@NotNull String s) {
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

}
