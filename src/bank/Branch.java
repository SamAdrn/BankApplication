package bank;

import utility.Address;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents a branch.<br><br>
 * <code>Branch</code> objects provide a variety of methods to simulate a real-life bank branch,
 * as in the different locations of operation held by the bank. Here, a data structure of
 * <code>Customer</code> objects is available, to store a handful of customers who registered as a
 * client of the branch.<br><br>
 * <code>Branch</code> objects are provided with a 3-Digit unique ID called <code>BRANCH_CODE</code>
 * at instantiation. This value is final and cannot be mutated.
 *
 * @author Samuel A. Kosasih
 *
 * @see Bank
 * @see Customer
 */
public class Branch implements Serialized, Iterable<Customer>, Serializable {

    /**
     * This field stores the name of the branch as a <code>String</code>.
     */
    private String branchName;

    /**
     * This field stores the address of the branch as an <code>Address</code> object.
     */
    private Address branchAddress;

    /**
     * This field stores the 3-Digit unique branch code as an <code>Integer</code>.
     */
    private final int BRANCH_CODE;

    /**
     * This field stores a <code>Map</code> used to store <code>Customer</code> objects.<br><br>
     * Uses the customer IDs as the key, and the <code>Customer</code> objects as the value.
     */
    private final Map<Integer, Customer> CUSTOMERS;

    /**
     * This field stores the number of customers as an <code>Integer</code>.
     */
    private int numberOfCustomers;

    /**
     * This field stores a <code>Random</code> object.<br><br>
     * Mainly used to generate the 3-Digit unique branch codes.
     */
    private final Random rand = new Random();

    /**
     * Default Constructor.<br><br>
     * Generates a 3-Digit unique ID used to distinguish between other <code>Branch</code>
     * objects. This ID is generated using the <code>Random</code> class.<br><br>
     * The recommended input for the address is as follows:
     * <br>
     * <blockquote>Street,City,State,Zip</blockquote>
     * with commas (,) separating each of them. An invalid address format will not break the
     * class, but will only store <em>Invalid Address</em> as the branch address.
     *
     * @param branchName    the branch's name as a <code>String</code>
     * @param branchAddress the branch's address as a <code>String</code>
     *
     * @see Random
     */
    public Branch(String branchName, String branchAddress) {
        this.branchName = branchName;
        this.branchAddress = new Address(branchAddress);
        this.CUSTOMERS = new LinkedHashMap<>();
        this.BRANCH_CODE = rand.nextInt(900) + 100;
        this.numberOfCustomers = 0;
    }

    /**
     * Overloaded Constructor. <br><br>
     * This constructor allows an <code>Address</code> object as a parameter.
     *
     * @param branchName    the Customer's name
     * @param branchAddress the Customer's address
     */
    public Branch(String branchName, Address branchAddress) {
        this.branchName = branchName;
        this.branchAddress = branchAddress;
        this.CUSTOMERS = new LinkedHashMap<>();
        this.BRANCH_CODE = rand.nextInt(900) + 100;
        this.numberOfCustomers = 0;
    }

    /**
     * Retrieves the branch's name.
     *
     * @return the branch's name as a <code>String</code>
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * Sets the branch's name with a new name.
     *
     * @param branchName the new given name as a <code>String</code>
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    /**
     * Retrieves the branch's address, formatted in the form:
     * <br>
     * <blockquote>Street, City, State Zip</blockquote>
     *
     * @return a representation of the branch's address as a <code>String</code>
     */
    public String getAddressString() {
        return branchAddress.toString();
    }

    /**
     * Retrieves the branch's address as an <code>Address</code> object.
     *
     * @return the branch's address object as an <code>Address</code>
     */
    public Address getAddress() {
        return branchAddress;
    }

    /**
     * Sets the branch's address to the <code>Address</code> parameter.
     *
     * @param branchAddress the new address as an <code>Address</code>.
     */
    public void setAddress(Address branchAddress) {
        this.branchAddress = branchAddress;
    }

    /**
     * Retrieves the 3-Digit <code>BRANCH_CODE</code>.
     *
     * @return a value for the branch code as an <code>Integer</code>
     */
    public int getBranchCode() {
        return BRANCH_CODE;
    }

    /**
     * Retrieves the number of customers registered at the branch.
     *
     * @return a value for the number of customers as an <code>Integer</code>
     */
    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    /**
     * Adds a new customer to be registered at the branch.<br><br>
     * The recommended input for the address is as follows:
     * <br>
     * <blockquote>Street,City,State,Zip</blockquote>
     * with commas (,) separating each of them. An invalid address format will not break the
     * class, but will only store <em>Invalid Address</em> as the customer address.
     *
     * @param customerName    the customer's name as a <code>String</code>
     * @param customerAddress the customer' address as a <code>String</code>
     * @return <code>true</code> if the new customer has been added to the database.
     * Otherwise, it will return <code>false</code>.
     *
     * @see Customer
     */
    public boolean addCustomer(String customerName, String customerAddress) {
        return addCustomer(customerName, new Address(customerAddress));
    }

    /**
     * Overloaded Method. Adds a new customer to be registered at the branch.<br><br>
     * This method accepts an <code>Address</code> object to set the customer's address
     * instead of a <code>String</code>.
     *
     * @param customerName    the customer's name as a <code>String</code>
     * @param customerAddress the customer' address as a <code>String</code>
     * @return <code>true</code> if the new customer has been added to the database.
     * Otherwise, it will return <code>false</code>.
     *
     * @see Customer
     */
    public boolean addCustomer(String customerName, Address customerAddress) {
        Customer customer = new Customer(customerName, customerAddress);
        while (true) {
            if (CUSTOMERS.containsKey(customer.getKey())) {
                customer = new Customer(customerName, customerAddress);
            } else {
                break;
            }
        }
        numberOfCustomers++;
        return CUSTOMERS.put(customer.getKey(), customer) == null;
    }

    /**
     * Removes a customer from the branch database.<br><br>
     * To choose the customer to be removed, the <code>customerId</code> parameter will identify
     * which <code>Customer</code> object to be removed based on its <code>CUSTOMER_ID</code>.
     *
     * @param customerId a 5-Digit customer ID as an <code>Integer</code> to identify the customer
     *                   to be removed
     * @return <code>true</code> if customer is found and successfully removed. Otherwise, it will
     * return <code>false</code>.
     *
     * @see Customer
     */
    public boolean removeCustomer(int customerId) {
        if (CUSTOMERS.remove(customerId) != null) {
            numberOfCustomers--;
            return true;
        }
        return false;
    }

    /**
     * Retrieves the <code>Customer</code> with the given customer ID.
     *
     * @param customerId the 5-digit customer ID of the customer to be retrieved as an <code>Integer</code>
     * @return the <code>Customer</code> object with the <code>customerId</code>, or <code>null</code> if
     * not found
     */
    public Customer getCustomer(int customerId) {
        return CUSTOMERS.get(customerId);
    }

    /**
     * Compares two <code>Branch</code> objects.<br><br>
     * Uses the <code>BRANCH_CODE</code> field to compare if the other branch <code>o</code>
     * has the same 3-Digit ID.
     *
     * @param o the other <code>Branch</code> object
     * @return <code>true</code> if they have matching <code>BRANCH_CODE</code>s, or if the other
     * branch is itself. Otherwise, it will return <code>false</code>.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return BRANCH_CODE == branch.BRANCH_CODE;
    }

    /**
     * Generates a hash code for the <code>Branch</code> object.<br><br>
     *
     * @return an <code>Integer</code> value for the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(BRANCH_CODE);
    }

    /**
     * Provides a <code>String</code> representation of the branch in the form:
     * <br>
     * <blockquote>Branch Name (Branch Code)<br>Address<br><blockquote>- Customers -</blockquote></blockquote>
     *
     * @return the representation of the branch as a <code>String</code>
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(branchName + " [" + BRANCH_CODE + "]");
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
     * Provides a simple <code>String</code> representation of the branch in the form:
     * <br>
     * <blockquote>[Branch Code] Branch Name</blockquote>
     *
     * @return the simplified branch representation as a <code>String</code>
     */
    public String simplifiedString() {
        return "[" + BRANCH_CODE + "] " + branchName;
    }

    /**
     * Retrieves non-negative <code>Integer</code> key, a 5-Digit branch code.
     *
     * @return the key as an <code>Integer</code>
     */
    @Override
    public int getKey() {
        return BRANCH_CODE;
    }

    /**
     * Returns an <code>Iterator</code> of type <code>Customer</code> to iterate
     * through the map of <code>Customer</code>s registered at the branch.
     *
     * @return an <code>Iterator</code> of type <code>Customer</code>
     */
    @Override
    public Iterator<Customer> iterator() {
        return CUSTOMERS.values().iterator();
    }

}
