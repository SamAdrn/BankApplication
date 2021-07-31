package ui.gui;

import bank.Bank;
import bank.Branch;
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
 * This class serves as the controller for <code>customersDialog.fxml</code>.
 * <br><br>
 * Handles the application logic in order to display a list of customers within a bank.
 *
 * @author Samuel A. Kosasih
 * @see javafx.scene.control.Dialog
 * @see Customer
 * @see Bank
 */
public class CustomersDialogController {

    /**
     * This field refers to the <code>ListView</code> used to present the customers.
     */
    @FXML
    private ListView<Customer> customerListView;

    /**
     * Populates the <code>ListView</code> with the customers of the selected bank.
     * <br><br>
     * The customer who called for the process will not appear in the <code>ListView</code>.
     *
     * @param selectedBank    the selected <code>Bank</code> object. The customers from this bank
     *                        will be displayed on the <code>ListView</code>
     * @param currentCustomer the <code>Customer</code> object who called the method. This customer will be
     *                        excluded from being displayed on the <code>ListView</code>.
     */
    public void populateCustomerListView(Bank selectedBank, Customer currentCustomer) {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        for (Branch branch : selectedBank) {
            for (Customer customer : branch) {
                if (!customer.equals(currentCustomer)) {
                    customers.add(customer);
                }
            }
        }
        SortedList<Customer> customerSortedList =
                new SortedList<>(customers, Comparator.comparing(Customer::getCustomerId));

        customerListView.setItems(customerSortedList);

        customerListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Customer> call(ListView<Customer> customerListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Customer customer, boolean b) {
                        super.updateItem(customer, b);
                        if (b) {
                            setText(null);
                        } else {
                            setText(customer.simplifiedString());
                        }
                    }
                };
            }
        });

        customerListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        customerListView.getSelectionModel().selectFirst();
    }

    /**
     * Retrieves the user's selected customer from the <code>ListView</code>.
     *
     * @return a <code>Customer</code> object
     */
    public Customer processResults() {
        return customerListView.getSelectionModel().getSelectedItem();
    }

}
