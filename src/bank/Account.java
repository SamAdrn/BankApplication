package bank;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.Random;

/**
 * Defines the methods required to establish a Bank Account distinguished with a
 * 9-digit Account number.
 *
 * @author Samuel Kosasih
 */
public class Account implements Serialized, Serializable {

    /**
     * A 9-Digit unique integer representing the Account number
     */
    private final int ACCOUNT_NUMBER;
    /**
     * Amount of money being held in the Account
     */
    private double balance;

    /**
     * Formats Strings to local currency.
     */
    private final NumberFormat currency = NumberFormat.getCurrencyInstance();

    /**
     * Default Constructor.
     */
    public Account() {
        Random rand = new Random();
        this.ACCOUNT_NUMBER = rand.nextInt(900000000) + 100000000;
        balance = 0;
    }

    /**
     * Retrieves the 9-Digit Account number.
     *
     * @return an integer value for the Account number
     */
    public int getAccountNumber() {
        return ACCOUNT_NUMBER;
    }

    /**
     * Retrieves the balance of the Account.
     *
     * @return a double value representing the balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Deposits the given amount of money to the Account.
     *
     * @param amount the given amount of money
     * @return true if deposit is successful
     */
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        this.balance += amount;
        return true;
    }

    /**
     * Withdraws the given amount of money from the Account.
     *
     * @param amount the given amount of money
     * @return true if withdrawal is successful
     */
    public boolean withdraw(double amount) {
        if (amount > balance || amount <= 0) {
            return false;
        }
        this.balance -= amount;
        return true;
    }

    /**
     * Transfers the given amount of money to another given Account.
     *
     * @param account the given Account
     * @param amount  the given amount of money
     * @return true if money transfer is successful
     */
    public boolean transfer(Account account, double amount) {
        if (!this.equals(account)) {
            if (withdraw(amount)) {
                account.deposit(amount);
                return true;
            }
        }
        return false;
    }

    /**
     * Compares two Accounts' Account numbers and reports whether they are equal.
     *
     * @param o the other Account
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return ACCOUNT_NUMBER == account.ACCOUNT_NUMBER;
    }

    /**
     * Generates a hash code for the Account.
     *
     * @return an integer value for the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(ACCOUNT_NUMBER);
    }

    /**
     * Provides a String representation of the Account in the form:
     * <br>Account Number: #########<br>Balance: $###.##
     *
     * @return the String representation
     */
    @Override
    public String toString() {
        return "Account Number: " + ACCOUNT_NUMBER + "\n" +
                "\tBalance: " + currency.format(balance);
    }

    /**
     * Retrieves non-negative integer key, a 9-Digit Account Number.
     *
     * @return the key
     */
    @Override
    public int getKey() {
        return ACCOUNT_NUMBER;
    }
}
