package bank;

import utility.Address;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents a bank.<br><br>
 * <code>Bank</code> objects provide a variety of methods to simulate a real-life bank corporation.
 * Here, a data structure of <code>Branch</code> objects is available, to store the various locations of operations
 * controlled by the bank. Using <code>Bank</code> objects, new branches can be opened up, or be closed and
 * removed from the database.<br><br>
 * <code>Bank</code> objects are provided with a 4-Digit unique ID called <code>BANK_ID</code>
 * at instantiation. This value is final and cannot be mutated.
 *
 * @author Samuel A. Kosasih
 *
 * @see data.BankManager
 * @see database.BankList
 * @see Branch
 */
public class Bank implements Serialized, Serializable, Iterable<Branch> {

    /**
     * This field stores the name of the bank as a <code>String</code>.
     */
    private String bankName;

    /**
     * This field stores the 4-Digit unique bank ID as an <code>Integer</code>.
     */
    private final int BANK_ID;

    /**
     * This field stores a <code>Map</code> used to store <code>Branch</code> objects.<br><br>
     * Uses the branch codes as the key, and the <code>Branch</code> objects as the value.
     */
    private final Map<Integer, Branch> BRANCHES;

    /**
     * This field stores the number of still in operation branches as an <code>Integer</code>.
     */
    private int numberOfBranches;

    /**
     * This field stores a <code>Random</code> object.<br><br>
     * Mainly used to generate the 4-Digit unique bank IDs.
     */
    private final Random rand = new Random();

    /**
     * Default Constructor.<br><br>
     * Generates a 5-Digit unique ID used to distinguish between other <code>Bank</code>
     * objects. This ID is generated using the <code>Random</code> class.
     *
     * @param bankName the bank's name as a <code>String</code>
     *
     * @see Random
     */
    public Bank(String bankName) {
        this.bankName = bankName;
        this.BANK_ID = rand.nextInt(9000) + 1000;
        this.BRANCHES = new LinkedHashMap<>();
        numberOfBranches = 0;
    }

    /**
     * Overloaded Constructor. <br><br>
     * This constructor allows a <code>Branch</code> map to be imported.<br><br>
     * The map must store <code>Branch</code> objects as its value, and use their <code>BRANCH_CODE</code>s
     * as a key.
     *
     * @param bankName the bank's name
     * @param branches the map of branches to be imported
     */
    public Bank(String bankName, Map<Integer, Branch> branches) {
        this.bankName = bankName;
        this.BANK_ID = rand.nextInt(9000) + 1000;
        this.BRANCHES = branches;
        numberOfBranches = 0;
    }

    /**
     * Retrieves the bank's name.
     *
     * @return the bank's name as a <code>String</code>
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * Sets the bank's name with a new name.
     *
     * @param bankName the new given name as a <code>String</code>
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * Retrieves the 4-Digit <code>BANK_ID</code>.
     *
     * @return a value for the bank ID as an <code>Integer</code>
     */
    public int getBankId() {
        return BANK_ID;
    }

    /**
     * Retrieves the number of branches in operation.
     *
     * @return a value for the number of branches as an <code>Integer</code>
     */
    public int getNumberOfBranches() {
        return numberOfBranches;
    }

    /**
     * Adds a new branch to be under the bank's operation.<br><br>
     * The recommended input for the address is as follows:
     * <br>
     * <blockquote>Street,City,State,Zip</blockquote>
     * with commas (,) separating each of them. An invalid address format will not break the
     * class, but will only store <em>Invalid Address</em> as the branch address.
     * <br><br>
     * Will not accept a name that belongs to an existing branch in the database.
     *
     * @param branchName    the branch's name as a <code>String</code>
     * @param branchAddress the branch' address as a <code>String</code>
     * @return <code>true</code> if the new branch has been added to the database.
     * Otherwise, it will return <code>false</code>.
     *
     * @see Branch
     */
    public boolean createBranch(String branchName, String branchAddress) {
        return createBranch(branchName, new Address(branchAddress));
    }

    /**
     * Overloaded Method. Adds a new branch to be under the bank's operation.<br><br>
     * This method accepts an <code>Address</code> object to set the branch's address
     * instead of a <code>String</code>.
     * <br><br>
     * Will not accept a name that belongs to an existing branch in the database.
     *
     * @param branchName    the branch's name as a <code>String</code>
     * @param branchAddress the branch' address as a <code>String</code>
     * @return <code>true</code> if the new branch has been added to the database.
     * Otherwise, it will return <code>false</code>.
     *
     * @see Branch
     */
    public boolean createBranch(String branchName, Address branchAddress) {
        for (Branch branch : this) {
            if (branch.getBranchName().equalsIgnoreCase(branchName)) {
                return false;
            }
        }
        Branch branch = new Branch(branchName, branchAddress);
        while (true) {
            if (BRANCHES.containsKey(branch.getKey())) {
                branch = new Branch(branchName, branchAddress);
            } else {
                break;
            }
        }
        numberOfBranches++;
        return BRANCHES.put(branch.getKey(), branch) == null;
    }

    /**
     * Removes a branch from the bank database.<br><br>
     * To choose the branch to be removed, the <code>branchCode</code> parameter will identify
     * which <code>Branch</code> object to be removed based on its <code>BRANCH_CODE</code>.
     *
     * @param branchCode a 3-Digit branch code as an <code>Integer</code> to identify the branch
     *                   to be removed
     * @return <code>true</code> if branch is found and successfully removed. Otherwise, it will
     * return <code>false</code>.
     *
     * @see Branch
     */
    public boolean removeBranch(int branchCode) {
        if (BRANCHES.remove(branchCode) != null) {
            numberOfBranches--;
            return true;
        }
        return false;
    }

    /**
     * Retrieves the <code>Branch</code> with the given branch code.
     *
     * @param branchCode the 3-digit code of the branch to be retrieved as an <code>Integer</code>
     * @return the <code>Branch</code> object with the <code>branchCode</code>, or <code>null</code> if
     * not found
     */
    public Branch getBranch(int branchCode) {
        return BRANCHES.get(branchCode);
    }

    /**
     * Compares two <code>Bank</code> objects.<br><br>
     * Uses the <code>BANK_ID</code> field to compare if the other bank <code>o</code>
     * has the same 4-Digit ID.
     *
     * @param o the other <code>Bank</code> object
     * @return <code>true</code> if they have matching <code>BANK_ID</code>s, or if the other
     * bank is itself. Otherwise, it will return <code>false</code>.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return BANK_ID == bank.BANK_ID;
    }

    /**
     * Generates a hash code for the <code>Bank</code> object.<br><br>
     *
     * @return an <code>Integer</code> value for the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(BANK_ID);
    }

    /**
     * Provides a <code>String</code> representation of the bank in the form:
     * <br>
     * <blockquote>Bank Name (Bank ID)<br><blockquote>- Branches -</blockquote></blockquote>
     *
     * @return the representation of the bank as a <code>String</code>
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(bankName + " [" + BANK_ID + "]");
        s.append("\n\t").append("Available Branches:");
        if (numberOfBranches == 0) {
            s.append("\n\t\t").append("- No branches available -");
        } else {
            for (Branch branch : this) {
                s.append("\n\t\t").append(branch.simplifiedString());
            }
        }
        return s.toString();
    }

    /**
     * Provides a simple <code>String</code> representation of the bank in the form:
     * <br>
     * <blockquote>[Bank ID] Bank Name</blockquote>
     *
     * @return the simplified bank representation as a <code>String</code>
     */
    public String simplifiedString() {
        return "[" + BANK_ID + "] " + bankName;
    }

    /**
     * Retrieves non-negative <code>Integer</code> key, a 5-Digit bank ID.
     *
     * @return the key as an <code>Integer</code>
     */
    @Override
    public int getKey() {
        return BANK_ID;
    }

    /**
     * Returns an <code>Iterator</code> of type <code>Branch</code> to iterate
     * through the map of <code>Branch</code>s under the bank's operation.
     *
     * @return an <code>Iterator</code> of type <code>Branch</code>
     */
    @Override
    public Iterator<Branch> iterator() {
        return BRANCHES.values().iterator();
    }
}
