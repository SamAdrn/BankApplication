package bank;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.Random;

/**
 * This class represents a customer's account.<br><br>
 * <code>Account</code> objects provide a variety of methods to simulate a real-life bank account. From
 * depositing and withdrawing money, to transferring funds from one account to another. <br><br>
 * <code>Account</code> objects are provided with a 9-Digit unique ID called <code>ACCOUNT_NUMBER</code> at
 * instantiation. This value is final and cannot be mutated. <br><br>
 *
 * @author Samuel A. Kosasih
 *
 * @see Customer
 */
public class Account implements Serialized, Serializable {

    /**
     * This field stores a 9-Digit unique ID representing the Account Number as an <code>Integer</code>.
     */
    private final int ACCOUNT_NUMBER;

    /**
     * This field stores the account balance as a <code>Double</code>.<br><br>
     * Represents the amount of funds currently in this account.
     */
    private double balance;

    /**
     * This field is a <code>NumberFormat</code> object variable used to format <code>Number</code>
     * variables to display as local currency.<br><br>
     * The currency format is chosen from the system's local currency configurations.
     */
    private final NumberFormat currency = NumberFormat.getCurrencyInstance();

    /**
     * Default Constructor. <br><br>
     * Generates a 9-Digit unique ID used to distinguish between other <code>Account</code>
     * objects. This ID is generated using the <code>Random</code> class.
     *
     * @see Random
     */
    public Account() {
        Random rand = new Random();
        this.ACCOUNT_NUMBER = rand.nextInt(900000000) + 100000000;
        balance = 0;
    }

    /**
     * Retrieves the 9-Digit <code>ACCOUNT_NUMBER</code>.
     *
     * @return a value for the account number as an <code>Integer</code>
     */
    public int getAccountNumber() {
        return ACCOUNT_NUMBER;
    }

    /**
     * Retrieves the amount of funds stored in the account.
     *
     * @return a value for the account balance as a <code>Double</code>
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Deposits the given amount of funds to the account.<br><br>
     * This method will not accept any value below, or equal to, zero (0) as a
     * parameter.
     *
     * @param amount the given amount of funds as a <code>Double</code>
     * @return <code>true</code> if value is above zero (0) and deposit is successful.
     * Otherwise, it will return <code>false</code>.
     */
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        this.balance += amount;
        return true;
    }

    /**
     * Withdraws the desired amount of funds from the account.<br><br>
     * This method will not accept any value below, or equal to, zero (0), or any
     * value that is greater than the account balance.
     *
     * @param amount the desired amount of funds as a <code>Double</code>
     * @return <code>true</code> if value meets criteria and withdrawal is successful.
     * Otherwise, it will return <code>false</code>.
     */
    public boolean withdraw(double amount) {
        if (amount > balance || amount <= 0) {
            return false;
        }
        this.balance -= amount;
        return true;
    }

    /**
     * Transfers the desired amount of funds to the other <code>account</code>.<br><br>
     * This method will not accept itself as a parameter, as it's illogical to transfer funds to
     * the same account.<br><br>
     * The process will also call the <code>withdraw()</code> method, which means that the value
     * for the <code>amount</code> must also follow the criteria:
     * <ul>
     *     <li>value must be greater than zero (0)</li>
     *     <li>value must be less than the account balance</li>
     * </ul>
     *
     * @param account the recipient <code>Account</code>. This account will be the destination
     *                of your funds.
     * @param amount  the given amount of funds as a <code>Double</code>
     * @return <code>true</code> if <code>account</code> differs from the origin account, and
     * the value for <code>amount</code> follows the criteria. Otherwise, it will return
     * <code>false</code>
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
     * Compares two <code>Account</code> objects.<br><br>
     * Uses the <code>ACCOUNT_NUMBER</code> field to compare if the other account <code>o</code>
     * has the same 9-Digit ID.
     *
     * @param o the other <code>Account</code> object
     * @return <code>true</code> if they have matching <code>ACCOUNT_NUMBER</code>s, or if the other
     * account is itself. Otherwise, it will return <code>false</code>.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return ACCOUNT_NUMBER == account.ACCOUNT_NUMBER;
    }

    /**
     * Generates a hash code for the <code>Account</code> object.<br><br>
     *
     * @return an <code>Integer</code> value for the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(ACCOUNT_NUMBER);
    }

    /**
     * Provides a <code>String</code> representation of the account in the form:
     * <br>
     * <blockquote>Account Number: #########<br>Balance: $###.##</blockquote>
     *
     * @return the representation of the account as a <code>String</code>
     */
    @Override
    public String toString() {
        return "Account Number: " + ACCOUNT_NUMBER + "\n" +
                "\tBalance: " + currency.format(balance);
    }

    /**
     * Retrieves non-negative <code>Integer</code> key, a 9-Digit account number.
     *
     * @return the key as an <code>Integer</code>
     */
    @Override
    public int getKey() {
        return ACCOUNT_NUMBER;
    }
}
