package database;

import bank.Bank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A database system to store a List of Bank objects.
 *
 * @author Samuel Kosasih
 */
public class BankList implements Serializable, Iterable<Bank> {

    /**
     * An ArrayList of Banks
     */
    private final ArrayList<Bank> banks;

    /**
     * Default Constructor.
     */
    public BankList() {
        this.banks = new ArrayList<>();
    }

    /**
     * Returns the number of banks stored in the BankList
     *
     * @return an integer value for size
     */
    public int getSize() {
        return banks.size();
    }

    /**
     * Adds the given Bank to the BankList.
     *
     * @param bank the given Bank
     * @return true if successful
     */
    public boolean add(Bank bank) {
        return banks.add(bank);
    }

    /**
     * Removes the Bank with the given Bank ID from the BankList.
     *
     * @param bankId the given Bank ID
     * @return true if successful
     */
    public boolean remove(int bankId) {
        return banks.removeIf(b -> b.getBankId() == bankId);
    }

    /**
     * Returns an iterator over elements of type Branch.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Bank> iterator() {
        return banks.iterator();
    }

}
