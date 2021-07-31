package ui.gui;

import bank.Bank;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * This class serves as the controller for <code>bankDialog.fxml</code>.
 * <br><br>
 * Handles the application logic in order to retrieve a new name for a <code>Bank</code> object,
 * when the user edits it.
 *
 * @author Samuel A. Kosasih
 *
 * @see javafx.scene.control.Dialog
 * @see Bank
 */
public class BankDialogController {

    /**
     * This field refers to the <code>Label</code> used to present the user with instructions.
     */
    @FXML
    private Label label;

    /**
     * This field refers to the <code>TextField</code> used to allow the user
     * to enter input.
     */
    @FXML
    private TextField bankName;

    /**
     * Retrieves the user's input from the <code>TextField</code>.
     *
     * @return the user's input as a <code>String</code>
     */
    public String processResults() {
        return bankName.getText().trim();
    }

    /**
     * Enables editing mode, with the <code>Bank</code> object to be edited specified
     * in the <code>selectedBank</code> parameter.
     *
     * @param selectedBank the selected <code>Bank</code> object
     */
    public void editMode(Bank selectedBank) {
        label.setText("Enter new name for bank");
        bankName.setText(selectedBank.getBankName());
        bankName.selectAll();
    }

}
