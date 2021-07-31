package ui.gui;

import bank.Branch;
import bank.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class serves as the controller for <code>withAddressDialog.fxml</code>.
 * <br><br>
 * Handles the application logic in order to retrieve a new name and address for
 * <code>Branch</code> and <code>Customer</code> objects.
 * <br><br>
 * Use the <code>Dialog</code> for editing the said objects.
 *
 * @author Samuel A. Kosasih
 *
 * @see javafx.scene.control.Dialog
 * @see Branch
 * @see Customer
 */
public class WithAddressController {

    /**
     * This field refers to the <code>Label</code>, presenting instructions for a new name.
     */
    @FXML
    private Label nameLabel;

    /**
     * This field refers to the <code>Label</code>, presenting instructions for a new address.
     */
    @FXML
    private Label addressLabel;

    /**
     * This field refers to the <code>TextField</code> where the user inputs a name.
     */
    @FXML
    private TextField name;

    /**
     * This field refers to the <code>TextField</code> where the user inputs a street address.
     */
    @FXML
    private TextField street;

    /**
     * This field refers to the <code>TextField</code> where the user inputs a city name.
     */
    @FXML
    private TextField city;

    /**
     * This field refers to the <code>ComboBox</code> of <code>String</code>s populated with
     * U.S state names for the user to choose from.
     */
    @FXML
    private ComboBox<String> state;

    /**
     * This field refers to the <code>TextField</code> where the user inputs a 5-Digit zip code.
     */
    @FXML
    private TextField zipCode;

    /**
     * This field refers to the <code>Label</code> used to notify the user for any input errors.
     */
    @FXML
    private Label statusLabel;

    /**
     * This field is a <code>Boolean</code> which indicates whether the input is good to go.
     */
    private Boolean valid = true;

    /**
     * Handles <code>Dialog</code> start-up.
     * <br><br>
     * <b>Incomplete - Filter user input</b>
     */
    public void initialize() {
        populateStates();
//        city.setTextFormatter(new TextFormatter<>(change -> {
//            if (change.getText().matches("^[0-9]*")) {
//                city.setStyle("-fx-shadow-highlight-color: red;");
//                statusLabel.setText("City must not contain numbers.");
//                valid = false;
//            } else {
//                city.setStyle("-fx-shadow-highlight-color: #0093ff;");
//                statusLabel.setText("");
//                valid = true;
//            }
//            return change;
//        }));
        //TODO create text filters
    }

//    @FXML
//    public void handleCityFilter() {
//        city.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\sa-zA-Z*")) {
//                city.setStyle("-fx-shadow-highlight-color: red;");
//                statusLabel.setText("City must not contain numbers.");
//                valid = false;
//            } else {
//                city.setStyle("-fx-shadow-highlight-color: #0093ff;");
//                statusLabel.setText("");
//                valid = true;
//            }
//        });
//    }
    //TODO create text filters

//    @FXML
//    public void handleZipCodeFilter() {
//        zipCode.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\d*")) {
//                zipCode.setStyle("-fx-shadow-highlight-color: red;");
//                statusLabel.setText("State must only contain numbers.");
//                valid = false;
//            } else if (zipCode.getText().length() > 5) {
//                String s = zipCode.getText().substring(0, 5);
//                zipCode.setText(s);
//            } else {
//                zipCode.setStyle("-fx-shadow-highlight-color: #0093ff;");
//                statusLabel.setText("");
//                valid = true;
//            }
//        });
//    }
    //TODO create text filters

    /**
     * Populates the <code>state ComboBox</code> with states read from a text file
     * <code>US_States.txt</code>/
     */
    private void populateStates() {
        File file = new File ("US_States.txt");
        try {
            Scanner read = new Scanner(file);
            ObservableList<String> states = FXCollections.observableArrayList();
            while (read.hasNextLine()) {
                states.add(read.nextLine());
            }
            state.setItems(states);
            state.setEditable(false);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("States not populated");
            alert.setContentText("File containing US_States not found. Please enter state name manually.");
            alert.show();
            state.setEditable(true);
            e.printStackTrace();
        }
    }

    /**
     * Call this method to use the <code>Dialog</code> for adding new customers
     */
    public void handleCustomers() {
        nameLabel.setText("Enter name of Customer:");
        addressLabel.setText("Enter Customer's Address:");
    }

    /**
     * Enables <code>Branch</code> editing mode.
     *
     * @param selectedBranch the <code>Branch</code> to be edited
     */
    public void editMode(Branch selectedBranch) {
        nameLabel.setText("Enter new name for branch");
        name.setText(selectedBranch.getBranchName());
        street.setText(selectedBranch.getAddress().getStreet());
        city.setText(selectedBranch.getAddress().getCity());
        state.getSelectionModel().select(selectedBranch.getAddress().getState());
        zipCode.setText(selectedBranch.getAddress().getZipCode());
    }

    /**
     * Enables <code>Customer</code> editing mode.
     *
     * @param selectedCustomer the <code>Customer</code> to be edited
     */
    public void editMode(Customer selectedCustomer) {
        nameLabel.setText("Enter new name for customer");
        name.setText(selectedCustomer.getName());
        street.setText(selectedCustomer.getAddress().getStreet());
        city.setText(selectedCustomer.getAddress().getCity());
        state.getSelectionModel().select(selectedCustomer.getAddress().getState());
        zipCode.setText(selectedCustomer.getAddress().getZipCode());
    }

    /**
     * Retrieves an array of <code>String</code>s containing the name, street address, city name,
     * state, and a 5-Digit zip code, in the same order.
     *
     * @return an array of <code>String</code>s
     */
    public String[] processResults() {
        if (!valid) {
            return null;
        }
        if (name.getText().trim().equals("") || street.getText().trim().equals("") || city.getText().trim().equals("") ||
                state.getSelectionModel().getSelectedItem() == null || zipCode.getText().trim().equals("")) {
            return null;
        }
        return new String[]{name.getText().trim(), street.getText().trim(), city.getText().trim(),
                state.getSelectionModel().getSelectedItem().trim(), zipCode.getText().trim()};
    }

}
