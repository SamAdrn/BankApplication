package data;

import bank.Bank;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * This class serves as a manager to handle a number of banks.<br><br>
 * Here, a data structure of <code>Bank</code> objects is available, to store a number of bank corporations
 * controlled by the user. Banks can be created, edited, removed, all from this class alone.<br><br>
 * This class also provides File I/0 methods to read an existing bank database from a file, or save a user's
 * session to one.
 *
 * @author Samuel A. Kosasih
 *
 * @see Bank
 * @see BankList
 */
public class BankManager implements Iterable<Bank>, Serializable {

    /**
     * This field stores a <code>BankList</code> object, a data structure to store <code>Bank</code> objects.
     */
    private BankList banks;

    /**
     * This field stores a <code>File</code> object used for file handling purposes.<br><br>
     * Refers to the file name <code>bankDatabase.ser</code> stored within the project files.
     */
    private final File file = new File("bankDatabase.ser");

    /**
     * Default Constructor.<br><br>
     * Reads from an object file referred by the file name <code>bankDatabase.ser</code> stored
     * within the project files. If not found, it will proceed with a new <code>BankList</code>.
     *
     * @see BankList
     */
    public BankManager() {
        if (!read()) {
            this.banks = new BankList();
        }
    }

    /**
     * Retrieves the number of bank corporations under the user's management.
     *
     * @return a value for the number of banks as an <code>Integer</code>
     */
    public int getNumberOfBanks() {
        return banks.getSize();
    }

    /**
     * Creates a new bank to be under the user's management.<br><br>
     * This method will first check whether <code>bankName</code> is unique, as it cannot store
     * duplicate banks with the same name.
     *
     * @param bankName the bank's name as a <code>String</code>
     * @return <code>true</code> if the new bank has been added to the database.
     * Otherwise, it will return <code>false</code>.
     *
     * @see Bank
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
     * Removes a bank from the database.<br><br>
     * To choose the bank to be removed, the <code>bankId</code> parameter will identify
     * which <code>Bank</code> object to be removed based on its <code>BANK_ID</code>.
     *
     * @param bankId a 4-Digit bank ID as an <code>Integer</code> to identify the bank
     *               to be removed
     * @return <code>true</code> if bank is found and successfully removed. Otherwise, it will
     * return <code>false</code>.
     *
     * @see Bank
     */
    public boolean removeBank(int bankId) {
        return banks.remove(bankId);
    }

    /**
     * Retrieves the <code>Bank</code> with the given bank ID.
     *
     * @param bankId the 4-digit bank ID of the bank to be retrieved as an <code>Integer</code>
     * @return the <code>Bank</code> object with the <code>bankId</code>, or <code>null</code> if
     * not found
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
     * Generates a hash code for the <code>BankManager</code> object.<br><br>
     *
     * @return an <code>Integer</code> value for the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(banks);
    }

    /**
     * Returns an <code>Iterator</code> of type <code>Bank</code> to iterate
     * through the <code>BankList</code>.
     *
     * @return an <code>Iterator</code> of type <code>Bank</code>
     *
     * @see BankList
     */
    @Override
    public Iterator<Bank> iterator() {
        return banks.iterator();
    }

    /**
     * Saves any changes made to the database within the session.<br><br>
     * This method will write the <code>BankList</code> object to a file referred by
     * the field <code>file</code>.
     *
     * @return <code>true</code> if the session is successfully saved. Otherwise it will return <code>false</code>
     *
     * @see BankList
     * @see ObjectOutputStream
     * @see FileOutputStream
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
     * Reads existing data to the <code>BankList</code> from a file referred by the field <code>file</code> for the current session.
     *
     * @return <code>true</code> if file is found and data is successfully read. Otherwise, it will return
     * <code>false</code>.
     *
     * @see BankList
     * @see ObjectInputStream
     * @see FileInputStream
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

    /**
     * This private inner class serves as a data structure used to store <code>Bank</code> objects.<br><br>
     * Since this program's file handling was designed to save the database of <code>Bank</code> objects,
     * this class serves that exact purpose. Using this class to be saved into an object file (.ser) is
     * much safer rather than saving a raw data structure.<br><br>
     * <b>Must only be used in the <code>BankManager</code> class</b>
     *
     * @author Samuel A. Kosasih
     *
     * @see Bank
     * @see data.BankManager
     */
    private static class BankList implements Serializable, Iterable<Bank> {

        /**
         * This field stores a <code>ArrayList</code> used to store <code>Bank</code> objects.<br><br>
         */
        private final ArrayList<Bank> banks;

        /**
         * Default Constructor.
         */
        public BankList() {
            this.banks = new ArrayList<>();
        }

        /**
         * Retrieves the number of <code>Bank</code> objects in the database.
         *
         * @return a value for the number of banks as an <code>Integer</code>
         */
        public int getSize() {
            return banks.size();
        }

        /**
         * Adds a new <code>Bank</code> object to the database.<br><br>
         *
         * @param bank the <code>Bank</code> object to be added
         * @return <code>true</code> if the new bank has been added to the database.
         * Otherwise, it will return <code>false</code>.
         */
        public boolean add(Bank bank) {
            return banks.add(bank);
        }

        /**
         * Removes a bank with the <code>bankId</code> from the database.<br><br>
         *
         * @param bankId a 4-Digit bank ID as an <code>Integer</code> of the bank
         *               to be removed
         * @return <code>true</code> if bank is found and successfully removed. Otherwise, it will
         * return <code>false</code>.
         *
         * @see Bank
         */
        public boolean remove(int bankId) {
            return banks.removeIf(b -> b.getBankId() == bankId);
        }

        /**
         * Returns an <code>Iterator</code> of type <code>Bank</code> to iterate
         * through the database of <code>Bank</code>s.
         *
         * @return an <code>Iterator</code> of type <code>Bank</code>
         */
        @Override
        public @NotNull Iterator<Bank> iterator() {
            return banks.iterator();
        }

    }

}
