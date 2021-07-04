package bank;

import utility.Address;

import java.io.Serializable;
import java.util.*;

/**
 * Defines the methods required to establish a Branch of a Bank distinguished
 * with a 3-Digit Branch Code.
 *
 * @author Samuel Kosasih
 */
public class Branch implements Serialized, Iterable<Customer>, Serializable {

    /**
     * The name of the Branch.
     */
    private final String BRANCH_NAME;
    /**
     * The address of the Branch.
     */
    private Address branchAddress;
    /**
     * A 3-Digit unique integer representing the Branch Code.
     */
    private final int BRANCH_CODE;
    /**
     * A Decimal Map of Customers in this Branch.
     */
    private final Map<Integer, Customer> CUSTOMERS;
    /**
     * The number of Customers associated with the Branch.
     */
    private int numberOfCustomers;
    /**
     * An Object of type Random used to generate Branch Codes.
     */
    private final Random rand = new Random();

    /**
     * Default Constructor.
     *
     * @param branchName    The Branch's name
     * @param branchAddress The Branch's address
     */
    public Branch(String branchName, String branchAddress) {
        this.BRANCH_NAME = branchName;
        this.branchAddress = new Address(branchAddress);
        this.CUSTOMERS = new LinkedHashMap<>();
        this.BRANCH_CODE = rand.nextInt(900) + 100;
        this.numberOfCustomers = 0;
    }

    /**
     * Overloaded Constructor. Allows the given Customer Decimal Map to be
     * imported immediately.
     *
     * @param branchName    the Branch's name
     * @param branchAddress the Branch's address
     * @param customers     the given Customer Decimal Map
     */
    public Branch(String branchName, String branchAddress, Map<Integer, Customer> customers) {
        this.BRANCH_NAME = branchName;
        this.branchAddress = new Address(branchAddress);
        this.CUSTOMERS = customers;
        this.BRANCH_CODE = rand.nextInt(900) + 100;
        this.numberOfCustomers = 0;
    }

    /**
     * Retrieves the Branch's name.
     *
     * @return a String value representing the Branch's name
     */
    public String getBranchName() {
        return BRANCH_NAME;
    }

    /**
     * Retrieves the Branch's address.
     *
     * @return a String value representing the Branch's address
     */
    public String getBranchAddress() {
        return branchAddress.toString();
    }

    /**
     * Sets the Customer's address to the new given address.
     *
     * @param branchAddress the new given address
     * @return returns true to indicate that the address has been renewed
     */
    public boolean setNewBranchAddress(String branchAddress) {
        this.branchAddress = new Address(branchAddress);
        return true;
    }

    /**
     * Retrieves the 3-Digit Branch Code
     *
     * @return an integer value for the Branch Code
     */
    public int getBranchCode() {
        return BRANCH_CODE;
    }

    /**
     * Retrieves the number of Customers associated with the Branch.
     *
     * @return an integer value for the number of Customers
     */
    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    /**
     * Adds a new Customer to be associated with the Branch.
     *
     * @param customerName    the Customer's name
     * @param customerAddress the Customer' address
     * @return true if new Customer has been added
     */
    public boolean addCustomer(String customerName, String customerAddress) {
        Customer customer = new Customer(customerName, customerAddress);
        while (true) {
            if (CUSTOMERS.containsKey(customer.getKey())) {
                customer = new Customer(customerName, customerAddress);
            } else {
                break;
            }
        }
        numberOfCustomers++;
        return CUSTOMERS.put(customer.getKey(), customer) != null;
    }

    /**
     * Removes a Customer with the given Customer ID for the Branch's Customer
     * list.
     *
     * @param customerId the given Customer ID
     * @return true if Customer is successfully removed
     */
    public boolean removeCustomer(int customerId) {
        if (CUSTOMERS.remove(customerId) != null) {
            numberOfCustomers--;
            return true;
        }
        return false;
    }

    /**
     * Retrieves the Customer with the given Customer ID.
     *
     * @param customerId the given Customer ID
     * @return the Customer with the given ID, or null if not found
     */
    public Customer getCustomer(int customerId) {
        return CUSTOMERS.get(customerId);
    }

    /**
     * Compares two Branch codes and reports whether they are equal.
     *
     * @param o the other Branch
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return BRANCH_CODE == branch.BRANCH_CODE;
    }

    /**
     * Generates a hash code for the Branch.
     *
     * @return an integer value for the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(BRANCH_CODE);
    }

    /**
     * Provides a String representation of the Branch in the form:
     * <br>Branch Name (Branch Code)<br>Address<br>- Customers -
     *
     * @return the String representation
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(BRANCH_NAME + " [" + BRANCH_CODE + "]");
        s.append("\n\t").append(branchAddress.toString());
        s.append("\n\t").append("Customers:");
        if (numberOfCustomers == 0) {
            s.append("\n\t\t").append("- No customers found -");
        } else {
            for (Customer customers : this) {
                s.append("\n\t\t").append(customers.simplifiedString());
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
        return "[" + BRANCH_CODE + "] " + BRANCH_NAME;
    }

    /**
     * Retrieves non-negative integer key, a 3-Digit Branch Code.
     *
     * @return the key
     */
    @Override
    public int getKey() {
        return BRANCH_CODE;
    }

    /**
     * Returns an iterator over elements of type Customers.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Customer> iterator() {
        return CUSTOMERS.values().iterator();
    }

}
