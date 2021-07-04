package bank;

import java.io.Serializable;
import java.util.*;

/**
 * Defines the methods required to establish a Bank Corporation distinguished
 * with a 4-Digit Bank ID.
 *
 * @author Samuel Kosasih
 */
public class Bank implements Serialized, Serializable, Iterable<Branch> {

    /**
     * The name of the Bank Corporation.
     */
    private final String BANK_NAME;
    /**
     * A 4-Digit unique integer representing the Bank ID.
     */
    private final int BANK_ID;
    /**
     * A Decimal Map of the Bank's Branches
     */
    private final Map<Integer, Branch> BRANCHES;
    /**
     * The number of Branches owned by the Bank.
     */
    private int numberOfBranches;
    /**
     * An Object of type Random used to generate Bank IDs.
     */
    private final Random rand = new Random();

    /**
     * Default Constructor.
     *
     * @param bankName the Bank's name.
     */
    public Bank(String bankName) {
        this.BANK_NAME = bankName;
        this.BANK_ID = rand.nextInt(9000) + 1000;
        this.BRANCHES = new LinkedHashMap<>();
    }

    /**
     * Overloaded Constructor. Allows the given Branch Decimal Map to be
     * imported immediately.
     *
     * @param bankName the Bank's name
     * @param branches the given Branch Decimal Map
     */
    public Bank(String bankName, Map<Integer, Branch> branches) {
        this.BANK_NAME = bankName;
        this.BANK_ID = rand.nextInt(9000) + 1000;
        this.BRANCHES = branches;
    }

    /**
     * Retrieves the Bank's name.
     *
     * @return a String value representing the Bank's name
     */
    public String getBankName() {
        return BANK_NAME;
    }

    /**
     * Retrieves the 4-Digit Bank ID
     *
     * @return an integer value for the Bank ID
     */
    public int getBankId() {
        return BANK_ID;
    }

    /**
     * Retrieves the number of Branches owned by the Bank.
     *
     * @return an integer value for the number of Branches
     */
    public int getNumberOfBranches() {
        return numberOfBranches;
    }

    /**
     * Creates a new Branch to be owned by the Bank.
     *
     * @param branchName    the name of the Branch
     * @param branchAddress the address of the Branch
     * @return true if new Branch is created and added successfully
     */
    public boolean createBranch(String branchName, String branchAddress) {
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
     * Removes the Branch with the given Branch Code from the list of the Bank's
     * Branches
     *
     * @param branchCode the given Branch Code
     * @return true if Branch is successfully removed
     */
    public boolean removeBranch(int branchCode) {
        if (BRANCHES.remove(branchCode) != null) {
            numberOfBranches--;
            return true;
        }
        return false;
    }

    /**
     * Retrieves the Branch with the given Branch Code.
     *
     * @param branchCode the given Branch Code
     * @return the Branch with the given code, or null if not found
     */
    public Branch getBranch(int branchCode) {
        return BRANCHES.get(branchCode);
    }

    /**
     * Compares two Bank IDs and reports whether they are equal.
     *
     * @param o the other Bank
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return BANK_ID == bank.BANK_ID;
    }

    /**
     * Generates a hash code for the Bank.
     *
     * @return an integer value for the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(BANK_ID);
    }

    /**
     * Provides a String representation of the Bank in the form:
     * <br>Bank Name (Bank ID)<br>- Branches -
     *
     * @return the String representation
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(BANK_NAME + " [" + BANK_ID + "]");
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
     * Provides a simple String representation of the Bank in the form:
     * <br>[Bank Id] Bank Name
     *
     * @return the simplified String representation
     */
    public String simplifiedString() {
        return "[" + BANK_ID + "] " + BANK_NAME;
    }

    /**
     * Retrieves non-negative integer key, a 4-Digit Bank ID.
     *
     * @return the key
     */
    @Override
    public int getKey() {
        return BANK_ID;
    }

    /**
     * Returns an iterator over elements of type Branch.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Branch> iterator() {
        return BRANCHES.values().iterator();
    }
}
