package bank;

import utility.Address;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

/**
 * Defines the methods required to represent a Bank Customer distinguished with a
 * 5-digit Customer ID.
 *
 * @author Samuel Kosasih
 */
public class Customer implements Serialized, Iterable<Account>, Serializable {

    /**
     * The Customer's name.
     */
    private final String NAME;
    /**
     * The address of the Customer.
     */
    private Address address;
    /**
     * A 5-Digit unique integer representing the Customer ID.
     */
    private final int CUSTOMER_ID;
    /**
     * An array of Accounts owned by the Customer.
     */
    private final Account[] ACCOUNTS;
    /**
     * The number of Accounts owned by the Customer.
     */
    private int numberOfAccounts;

    /**
     * Default Constructor.
     *
     * @param name    the Customer's name
     * @param address the Customer's address
     */
    public Customer(String name, String address) {
        this.NAME = name;
        this.address = new Address(address);
        ACCOUNTS = new Account[5];
        numberOfAccounts = 0;
        Random rand = new Random();
        this.CUSTOMER_ID = rand.nextInt(90000) + 10000;
    }

    /**
     * Retrieves the Customer's name.
     *
     * @return a String value representing the Customer's name
     */
    public String getName() {
        return NAME;
    }

    /**
     * Retrieves the Customer's address.
     *
     * @return a String value representing the Customer's address
     */
    public String getAddress() {
        return this.address.toString();
    }

    /**
     * Sets the Customer's address to the new given address.
     *
     * @param address the new given address
     * @return returns true to indicate that the address has been renewed
     */
    public boolean setNewAddress(String address) {
        this.address = new Address(address);
        return true;
    }

    /**
     * Retrieves the number of Accounts owned by the Customer.
     *
     * @return an integer value for the number of Accounts
     */
    public int getNumberOfAccounts() {
        return numberOfAccounts;
    }

    /**
     * Retrieves the 5-Digit Customer ID
     *
     * @return an integer value for the Customer ID
     */
    public int getCustomerId() {
        return this.CUSTOMER_ID;
    }

    /**
     * Creates a new Account with a unique 9-Digit Account Number. Customers are
     * limited to a possession of only 5 different Accounts.
     *
     * @return the newly-generated Account.
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
     * Deletes the Account that is owned by the Customer.
     *
     * @param account the Account to be deleted
     * @return true if deletion is successful
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
        for (int i = index; i < numberOfAccounts; i++) {
            ACCOUNTS[i] = ACCOUNTS[i + 1];
        }
        numberOfAccounts--;
        return true;
    }

    /**
     * Retrieves the Account with the given Account number.
     *
     * @param accountNumber the given Account number
     * @return an Account with matching details, ot null if not found
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
     * A helper method to verify that a given Account already exist in the
     * Accounts array.
     *
     * @param key the given Account's key to be checked for its existence
     * @return true if it exists
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
     * Compares two Customers' IDs and reports whether they are equal.
     *
     * @param o the other Customer
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return CUSTOMER_ID == customer.CUSTOMER_ID;
    }

    /**
     * Generates a hash code for the Customer.
     *
     * @return an integer value for the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(CUSTOMER_ID);
    }

    /**
     * Provides a String representation of the Customer in the form:
     * <br>Name (Customer ID)<br>Address<br>- Accounts -
     *
     * @return the String representation
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(NAME + " (" + CUSTOMER_ID + ")");
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
     * Provides a simple String representation of the Branch in the form:
     * <br>[Branch Code] Branch Name
     *
     * @return the simplified String representation
     */
    public String simplifiedString() {
        return "(" + CUSTOMER_ID + ") " + NAME;
    }

    /**
     * Retrieves non-negative integer key, a 5-Digit Customer ID.
     *
     * @return the key
     */
    @Override
    public int getKey() {
        return CUSTOMER_ID;
    }

    /**
     * Returns an iterator over elements of type Account.
     *
     * @return an Iterator for Accounts
     */
    @Override
    public Iterator<Account> iterator() {
        return new AccountIterator();
    }

    /**
     * A private inner class outlining the methods for an Account iterator.
     */
    private class AccountIterator implements Iterator<Account> {

        /**
         * The index location of the cursor.
         */
        private int currentIndex;

        /**
         * Default Constructor.
         */
        public AccountIterator() {
            currentIndex = 0;
        }

        /**
         * Determines if there is a next value in the array.
         *
         * @return true if there is a next value
         */
        @Override
        public boolean hasNext() {
            return currentIndex < numberOfAccounts;
        }

        /**
         * Provides access to the next value in the array.
         *
         * @return the next value
         */
        @Override
        public Account next() {
            currentIndex++;
            return ACCOUNTS[currentIndex - 1];
        }
    }

}
