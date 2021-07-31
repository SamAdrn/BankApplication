package ui.gui;

import bank.Account;
import bank.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.util.Callback;

import java.util.Comparator;

/**
 * This class serves as the controller for <code>accountsDialog.fxml</code>.
 * <br><br>
 * Handles the application logic in order to display a customer's list of accounts.
 *
 * @author Samuel A. Kosasih
 *
 * @see javafx.scene.control.Dialog
 * @see Customer
 * @see Account
 */
public class AccountsDialogController {

    /**
     * This field refers to the <code>ListView</code> used to present the accounts.
     */
    @FXML
    private ListView<Account> accountListView;

    /**
     * Populates the <code>ListView</code> with the accounts of the selected customer.
     *
     * @param selectedCustomer the selected <code>Customer</code> object. This customer's accounts
     *                         will be displayed on the <code>ListView</code>
     */
    public void populateAccountListView(Customer selectedCustomer) {
        ObservableList<Account> accounts = FXCollections.observableArrayList();
        for (Account account : selectedCustomer) {
            accounts.add(account);
        }
        SortedList<Account> accountsSortedList =
                new SortedList<>(accounts, Comparator.comparing(Account::getAccountNumber));

        accountListView.setItems(accountsSortedList);

        accountListView.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
            @Override
            public ListCell<Account> call(ListView<Account> customerListView) {
                ListCell<Account> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Account account, boolean b) {
                        super.updateItem(account, b);
                        if (b) {
                            setText(null);
                        } else {
                            setText("Account #" + account.getAccountNumber());
                        }
                    }
                };
                return cell;
            }
        });

        accountListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        accountListView.getSelectionModel().selectFirst();
    }

    /**
     * Retrieves the account selected by the user from the <code>ListView</code>.
     *
     * @return an <code>Account</code> object
     */
    public Account processResults() {
        return accountListView.getSelectionModel().getSelectedItem();
    }
}
