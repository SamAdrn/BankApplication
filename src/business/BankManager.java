package business;

import bank.Bank;
import database.BankList;

import java.io.*;
import java.util.Iterator;
import java.util.Objects;

/**
 * Creates an organizer for various banks.
 *
 * @author Samuel Kosasih
 */
public class BankManager implements Iterable<Bank>, Serializable {

    /**
     * An ArrayList of Bank Objects.
     */
    private BankList banks;
    /**
     * A File object to trace the path where saved Lists of Bank objects are stored
     */
    private final File file = new File("bankDatabase.ser");

    /**
     * Default Constructor.
     */
    public BankManager() {
        if (!read()) {
            this.banks = new BankList();
        }
    }

    /**
     * Retrieves the number of Banks stored in the ArrayList.
     *
     * @return the number of Banks
     */
    public int getNumberOfBanks() {
        return banks.getSize();
    }

    /**
     * Creates a new Bank with given name.
     *
     * @param bankName the given name.
     * @return true if the new Bank is successfully created
     */
    public boolean createBank(String bankName) {
        for (Bank banks : this) {
            if (banks.getBankName().equalsIgnoreCase(bankName)) {
                return false;
            }
        }
        banks.add(new Bank(bankName));
        return true;
    }

    /**
     * Removes the bank with the given Bank ID.
     *
     * @param bankId the given Bank ID
     * @return true if the Bank is successfully deleted
     */
    public boolean removeBank(int bankId) {
        return banks.remove(bankId);
    }

    /**
     * Retrieves the Bank with the given Bank ID.
     *
     * @param bankId the given Bank ID
     * @return the Bank with the given ID, or null if not found
     */
    public Bank getBank(int bankId) {
        for (Bank banks : this) {
            if (banks.getBankId() == bankId) {
                return banks;
            }
        }
        return null;
    }

    /**
     * Generates a hash code for the BankManager class.
     *
     * @return an integer value for the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(banks);
    }

    /**
     * Returns an iterator over elements of type Bank.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Bank> iterator() {
        return banks.iterator();
    }

    /**
     * Allows the List of Bank objects to be saved in a file for future access.
     *
     * @return true if object is successfully saved
     */
    public boolean save() {
        try (ObjectOutputStream output =
                     new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(banks);
            return true;
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            return false;
        }
    }

    /**
     * Allows an existing List of Bank objects to be read into the program.
     *
     * @return true if List of Bank objects is successfully written
     */
    private boolean read() {
        if (file.exists()) {
            try (ObjectInputStream input =
                         new ObjectInputStream(new FileInputStream(file))) {
                this.banks = (BankList) input.readObject();
                return true;
            } catch (IOException | ClassNotFoundException e) {
                return false;
            }
        }
        return false;
    }

}
