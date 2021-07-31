package ui.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.text.NumberFormat;

/**
 * This class serves as the controller for <code>amountDialog.fxml</code>.
 * <br><br>
 * Handles the application logic to retrieve numeric values from a <code>Dialog</code>.
 *
 * @author Samuel A. Kosasih
 * @see javafx.scene.control.Dialog
 */
public class AmountDialogController {

    /**
     * This field refers to the <code>TextField</code> where the user inputs the desired amount.
     */
    @FXML
    private TextField amount;

    /**
     * This field refers to the <code>Label</code> used to display the account balance of the user.
     */
    @FXML
    private Label balance;

    /**
     * This field stores a <code>NumberFormat</code> object to format numbers to the system's local
     * currency.
     */
    private static final NumberFormat currency = NumberFormat.getCurrencyInstance();

    /**
     * Handles <code>Dialog</code> start-up.
     * <br><br>
     * <b>Incomplete - Filter user input for numbers only</b>
     */
    public void initialize() {
        amount.requestFocus();
//        amount.setTextFormatter(new TextFormatter<Object>(change -> {
//            if (!change.getText().matches("/\\(?[\\d.]+\\)?/")) {
//                change.setText("");
//            }
//            return change;
//        }));
        //TODO - Create text filters
    }

    /**
     * Sets the <code>balance</code> <code>Label</code> to display the amount
     * in currency format.
     *
     * @param amount the amount of money available in the account. This value will displayed
     *               in the <code>Dialog</code>
     * @see bank.Account
     */
    public void setBalance(double amount) {
        balance.setText(currency.format(amount));
    }

    /**
     * Retrieves the amount entered by the user.
     *
     * @return the amount as a <code>Double</code>
     */
    public double processResults() {
        return Double.parseDouble(amount.getText());
    }

}
