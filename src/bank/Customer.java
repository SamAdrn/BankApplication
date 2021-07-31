package bank;

import utility.Address;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

/**
 * This class represents a customer.<br><br>
 * <code>Customer</code> objects provide a variety of methods to simulate a real-life bank
 * customer. Here, a data structure of <code>Account</code> objects is available, as well as
 * the power to open new accounts or close existing ones. A customer is limited to a maximum
 * of only <b>five (5)</b> accounts open simultaneously.<br><br>
 * <code>Customer</code> objects are provided with a 5-Digit unique ID called <code>CUSTOMER_ID</code>
 * at instantiation. This value is final and cannot be mutated.
 *
 * @author Samuel A. Kosasih
 *
 * @see Branch
 * @see Account
 */
public class Customer implements Serialized, Iterable<Account>, Serializable {

    /**
     * This field stores the name of the customer as a <code>String</code>.
     */
    private String name;

    /**
     * This field stores the address of the customer as an <code>Address</code> object.
     */
    private Address address;

    /**
     * This field stores the 5-Digit unique customer ID as an <code>Integer</code>.
     */
    private final int CUSTOMER_ID;

    /**
     * This field stores an array of type <code>Account</code>.<br><br>
     * Stores the customer's accounts, but only those which are currently still open.
     */
    private final Account[] ACCOUNTS;

    /**
     * This field stores the number of accounts, which are still open, as an <code>Integer</code>.
     */
    private int numberOfAccounts;

    /**
     * Default Constructor.<br><br>
     * Generates a 5-Digit unique ID used to distinguish between other <code>Customer</code>
     * objects. This ID is generated using the <code>Random</code> class.<br><br>
     * The recommended input for the address is as follows:
     * <br>
     * <blockquote>Street,City,State,Zip</blockquote>
     * with commas (,) separating each of them. An invalid address format will not break the
     * class, but will only store <em>Invalid Address</em> as the customer address.
     *
     * @param name    the customer's name as a <code>String</code>
     * @param address the customer's address as a <code>String</code>
     *
     * @see Random
     */
    public Customer(String name, String address) {
        this.name = name;
        this.address = new Address(address);
        ACCOUNTS = new Account[5];
        numberOfAccounts = 0;
        Random rand = new Random();
        this.CUSTOMER_ID = rand.nextInt(90000) + 10000;
        openAccount();
    }

    /**
     * Overloaded Constructor. <br><br>
     * This constructor allows an <code>Address</code> object as a parameter.
     *
     * @param name    the customer's name
     * @param address the customer's address
     */
    public Customer(String name, Address address) {
        this.name = name;
        this.address = address;
        ACCOUNTS = new Account[5];
        numberOfAccounts = 0;
        Random rand = new Random();
        this.CUSTOMER_ID = rand.nextInt(90000) + 10000;
        openAccount();
    }

    /**
     * Retrieves the customer's name.
     *
     * @return the customer's name as a <code>String</code>
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the customer's name with a new name.
     *
     * @param customerName the new given name as a <code>String</code>
     */
    public void setName(String customerName) {
        this.name = customerName;
    }

    /**
     * Retrieves the customer's address, formatted in the form:
     * <br>
     * <blockquote>Street, City, State Zip</blockquote>
     *
     * @return a representation of the customer's address as a <code>String</code>
     */
    public String getAddressString() {
        return this.address.toString();
    }

    /**
     * Retrieves the customer's address as an <code>Address</code> object.
     *
     * @return the customer's address object as an <code>Address</code>
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the customer's address to the <code>Address</code> parameter.
     *
     * @param customerAddress the new address as an <code>Address</code>.
     */
    public void setAddress(Address customerAddress) {
        this.address = customerAddress;
    }

    /**
     * Retrieves the number of accounts, which are currently open, owned by the customer.
     *
     * @return a value for the number of accounts as an <code>Integer</code>
     */
    public int getNumberOfAccounts() {
        return numberOfAccounts;
    }

    /**
     * Retrieves the 5-Digit <code>CUSTOMER_ID</code>.
     *
     * @return a value for the customer ID as an <code>Integer</code>
     */
    public int getCustomerId() {
        return this.CUSTOMER_ID;
    }

    /**
     * Creates a new account with a unique 9-Digit account number. <br><br>
     * The new <code>Account</code> object will then be stored in the accounts database, incrementing
     * the number of accounts owned by the customer.<br><br>
     * Customers are limited to a possession of only five (5) different accounts.
     *
     * @return the newly-generated <code>Account</code> object
     */
    public Account openAccount() {
        if (numberOfAccounts < 5) {
            Account a;
            do {
                a = new Account();
            } while (contains(a.getKey()));
            ACCOUNTS[numberOfAccounts] = a;
            numberOfAccounts++;
            return a;
        }
        return null;
    }

    /**
     * Closes the account and removes it from the database.<br><br>
     * Closing an account is only allowed when <b>the account balance is zero (0)</b>.
     *
     * @param account the account to be deleted as an <code>Account</code>
     * @return true if <code>account</code> exists in the database and its funds have been
     * withdrawn completely.
     */
    public boolean closeAccount(Account account) {
        Account a = null;
        int index = -1;
        for (int i = 0; i < numberOfAccounts; i++) {
            if (ACCOUNTS[i].equals(account)) {
                a = ACCOUNTS[i];
                index = i;
                break;
            }
        }
        if (a == null) {
            return false;
        }
        if (a.getBalance() != 0) {
            return false;
        }
        if (index == 4) {
            ACCOUNTS[4] = null;
        } else {
            if (numberOfAccounts - index >= 0) {
                System.arraycopy(ACCOUNTS, index + 1, ACCOUNTS, index, numberOfAccounts - index);
            }
        }
        numberOfAccounts--;
        return true;
    }

    /**
     * Retrieves the <code>Account</code> with the given account number.
     *
     * @param accountNumber the given Account number as an <code>Integer</code>
     * @return the <code>Account</code> object with the <code>accountNumber</code>, or <code>null</code>
     * if not found
     */
    public Account getAccount(int accountNumber) {
        if (Integer.toString(accountNumber).length() == 9) {
            for (Account a : ACCOUNTS) {
                if (a.getAccountNumber() == accountNumber) {
                    return a;
                }
            }
        }
        return null;
    }

    /**
     * A helper method to check if an <code>Account</code> with a matching
     * <code>ACCOUNT_NUMBER</code> exists in the <code>ACCOUNTS</code> array.
     *
     * @param key the <code>ACCOUNT_NUMBER</code> to be compared for its existence
     * @return <code>true</code> if an <code>Account</code> is already using the
     * the <code>ACCOUNT_NUMBER</code> specified in <code>key</code>. Otherwise,
     * it will return <code>false</code>.
     */
    private boolean contains(int key) {
        if (numberOfAccounts == 0) {
            return false;
        }
        for (Account acc : ACCOUNTS) {
            if (acc != null) {
                if (acc.getKey() == key) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Compares two <code>Customer</code> objects.<br><br>
     * Uses the <code>CUSTOMER_ID</code> field to compare if the other customer <code>o</code>
     * has the same 5-Digit ID.
     *
     * @param o the other <code>Customer</code> object
     * @return <code>true</code> if they have matching <code>CUSTOMER_ID</code>s, or if the other
     * customer is itself. Otherwise, it will return <code>false</code>.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return CUSTOMER_ID == customer.CUSTOMER_ID;
    }

    /**
     * Generates a hash code for the <code>Customer</code> object.<br><br>
     *
     * @return an <code>Integer</code> value for the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(CUSTOMER_ID);
    }

    /**
     * Provides a <code>String</code> representation of the customer in the form:
     * <br>
     * <blockquote>Name (Customer ID)<br>Address<br><blockquote>- Accounts -</blockquote></blockquote>
     *
     * @return the representation of the customer as a <code>String</code>
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(name + " (" + CUSTOMER_ID + ")");
        s.append("\n").append(address.toString());
        if (numberOfAccounts == 0) {
            s.append("\n\t").append("No accounts open");
        } else {
            for (Account account : ACCOUNTS) {
                if (account != null) {
                    s.append("\n\t").append(account.toString());
                }
            }
        }
        return s.toString();
    }

    /**
     * Provides a simple <code>String</code> representation of the customer in the form:
     * <br>
     * <blockquote>(Customer ID) Customer Name</blockquote>
     *
     * @return the simplified customer representation as a <code>String</code>
     */
    public String simplifiedString() {
        return "(" + CUSTOMER_ID + ") " + name;
    }

    /**
     * Retrieves non-negative <code>Integer</code> key, a 5-Digit customer ID.
     *
     * @return the key as an <code>Integer</code>
     */
    @Override
    public int getKey() {
        return CUSTOMER_ID;
    }

    /**
     * Returns an <code>Iterator</code> of type <code>Account</code> to iterate
     * through the array of <code>Account</code>s owned by the customer.
     *
     * @return an <code>Iterator</code> of type <code>Account</code>
     */
    @Override
    public Iterator<Account> iterator() {
        return new AccountIterator();
    }

    /**
     * This private inner class outlines the methods for an <code>Iterator</code> of type
     * <code>Account</code> to iterate through the array of <code>Account</code>s, specifically
     * the <code>ACCOUNTS</code> array.
     */
    private class AccountIterator implements Iterator<Account> {

        /**
         * This field stores the location of the cursor as an <code>Integer</code>.<br><br>
         * The cursor is used to point at different index locations of the <code>ACCOUNTS</code>
         * array.
         */
        private int currentIndex;

        /**
         * Default Constructor.
         */
        public AccountIterator() {
            currentIndex = 0;
        }

        /**
         * Determines if there is a value in the next position of the array.
         *
         * @return <code>true</code> if there is a next value. If the cursor
         * has reached the end of the array, it will return <code>false</code>.
         */
        @Override
        public boolean hasNext() {
            return currentIndex < numberOfAccounts;
        }

        /**
         * Retrieves the value in the array at where the cursor is pointing, and
         * moves the cursor one step ahead..
         *
         * @return the <code>Account</code> stored at the cursor location
         */
        @Override
        public Account next() {
            currentIndex++;
            return ACCOUNTS[currentIndex - 1];
        }
    }

}
