package ui.gui;

import bank.Account;
import bank.Bank;
import bank.Branch;
import bank.Customer;
import data.BankManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import utility.Address;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Optional;

/**
 * This class serves as the controller class for <code>mainWindow.fxml</code>
 * <br><br>
 * Handles the application logic for the main window, allowing users to navigate through the UI and
 * build dialogs whenever required.
 *
 * @author Samuel A. Kosasih
 *
 * @see Dialog
 */
public class Controller {

    /**
     * This field stores a <code>BankManager</code> object.<br><br>
     * Serves to handle <code>Bank</code>-related operations, as well as a database.
     */
    private final BankManager manager = new BankManager();

    /**
     * This field stores a <code>NumberFormat</code> object to format numbers to the system's local
     * currency.
     */
    private static final NumberFormat currency = NumberFormat.getCurrencyInstance();

    /**
     * This field stores a <code>Bank</code> object to keep track of the user's selected bank.
     */
    private Bank selectedBank;

    /**
     * This field stores a <code>Branch</code> object to keep track of the user's selected branch.
     */
    private Branch selectedBranch;

    /**
     * This field stores a <code>Customer</code> object to keep track of the user's selected customer.
     */
    private Customer selectedCustomer;

    /**
     * This field stores a <code>Account</code> object to keep track of the user's selected bank account.
     */
    private Account selectedAccount;

    /**
     * This field refers to the main <code>BorderPane</code> of our window.
     */
    @FXML
    private BorderPane mainPane;

    /**
     * This field refers to the <code>Label</code> in our <code>HBox</code> located
     * at the <code>bottom</code> position of our main <code>BorderPane</code>.
     */
    @FXML
    private Label statusLabel;

    /**
     * This field refers to the first <code>Button</code> in our <code>ToolBar</code> located
     * at the <code>top</code> position of our main <code>BorderPane</code>.
     * <br><br>
     * Serves as a button to create new objects.
     */
    @FXML
    private Button newButton;

    /**
     * This field refers to the third <code>Button</code> in our <code>ToolBar</code> located
     * at the <code>top</code> position of our main <code>BorderPane</code>.
     * <br><br>
     * Serves as a button to delete objects.
     */
    @FXML
    private Button deleteButton;

    /**
     * This field refers to the fourth <code>Button</code> in our <code>ToolBar</code> located
     * at the <code>top</code> position of our main <code>BorderPane</code>.
     * <br><br>
     * Serves as a button to return to the previous pane/menu.
     */
    @FXML
    private Button backButton;

    /**
     * This field refers to a <code>Button</code> that exists in every pane, which calls the respective
     * edit methods of each object type.
     */
    Button editButton = new Button("Edit Details");

    /**
     * Handles application start-up.
     * <br><br>
     * This method is first called when the <code>GuiMain</code> class loads the <code>mainWindow.fxml</code>
     * file into the <code>Scene</code>.
     * <br><br>
     * Ensures that the window shows the bank menu at startup.
     *
     * @see GuiMain
     */
    public void initialize() {
        showBanks();
    }

    /**
     * Presents a <code>Dialog</code> with instructions to use the application.
     *
     * @see Dialog
     */
    @FXML
    private void showInstructionsDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Instructions");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("instructionsDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            showDialogError();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        dialog.show();
    }

    /**
     * Creates a <code>Pane</code> that is used as a display when there are no objects to display
     * in a menu.
     *
     * @return a <code>VBox</code> with a <code>Label</code> displaying the text <em>Nothing to see here</em>
     *
     * @see VBox
     */
    private Pane createEmptyPane() {
        VBox pane = new VBox();
        Label label = new Label();
        label.setText("Nothing to see here");
        label.setTextFill(Color.DARKGRAY);
        pane.getChildren().add(label);
        pane.setAlignment(Pos.CENTER);
        return pane;
    }

    /**
     * Prepares the GUI to perform <code>Bank</code>-related tasks.
     * <br><br>
     * Will present a pane consisting of all the banks displayed using <code>Button</code>s,
     * and set the <code>newButton</code> to create a new <code>Bank</code> object.
     * <br><br>
     * The <code>backButton</code> and <code>deleteButton</code> will be disabled here,
     * since the user has made no selection at this point.
     *
     * @see Bank
     */
    private void showBanks() {
        backButton.setVisible(false);
        deleteButton.setVisible(false);
        newButton.setOnAction(actionEvent -> showNewBankDialog());
        newButton.setTooltip(new Tooltip("Add a new bank"));
        mainPane.setCenter(createBankPane());
        statusLabel.setText("Choose a Bank!");
    }

    /**
     * Creates a <code>Pane</code> object to present the user all the <code>Bank</code> objects
     * in the database.
     * <br><br>
     * The pane consists of a <code>VBox</code> as a header to welcome the user to the application,
     * since this is the first pane to be shown when the application starts. The rest will be a
     * <code>FlowPane</code> fitted inside a <code>ScrollPane</code>, filled with <code>Button</code>
     * objects, where each button represents a bank.
     * <br><br>
     * Clicking on a button will bring the user to a new pane, where the user is provided with options
     * to manage the selected bank.
     *
     * @return the contents described above in a <code>FlowPane</code>
     *
     * @see FlowPane
     * @see VBox
     * @see ScrollPane
     * @see Bank
     */
    private Pane createBankPane() {
        FlowPane pane = new FlowPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setHgap(10);
        pane.setVgap(10);

        VBox header = new VBox(10);
        header.setMinWidth(850);
        Label welcome = new Label("Welcome to Bank Application");
        welcome.setFont(Font.font("Verdana", FontWeight.LIGHT, 30));
        Label instruction = new Label("Choose a bank to begin, or create a new one!");
        instruction.setFont(Font.font("Verdana", FontWeight.EXTRA_LIGHT, 15));
        header.getChildren().addAll(welcome, instruction);

        Separator separator = new Separator();
        separator.setMinWidth(830);
        pane.getChildren().addAll(header, separator);

        if (manager.getNumberOfBanks() == 0) {
            statusLabel.setText("Add a new bank to begin!");
            pane.getChildren().add(createEmptyPane());
            return pane;
        }

        FlowPane buttons = new FlowPane();
        buttons.setHgap(10);
        buttons.setVgap(10);

        for (Bank bank : manager) {
            Button button = new Button(bank.getBankName() + " [" + bank.getBankId() + "]");
            button.setPrefSize(195, 50);
            button.setOnAction(actionEvent -> showBranches(manager.getBank(bank.getBankId())));
            button.setTooltip(new Tooltip("Branches available: " + bank.getNumberOfBranches()));
            buttons.getChildren().add(button);
        }
        pane.getChildren().add(createScrollPane(buttons));

        return pane;
    }

    /**
     * Prepares the GUI to perform <code>Branch</code>-related tasks.
     * <br><br>
     * Will present a pane consisting of all the branches owned by <code>bank</code> displayed
     * using <code>Button</code>s, and set the <code>newButton</code> to create a new
     * <code>Branch</code> object.
     * <br><br>
     * The <code>backButton</code> and <code>deleteButton</code> will be enabled here,
     * used for handling reselection and bank removal respectively.
     *
     * @param bank the selected <code>Bank</code> object
     *
     * @see Branch
     */
    private void showBranches(Bank bank) {
        selectedBank = bank;
        backButton.setVisible(true);
        deleteButton.setVisible(true);
        deleteButton.setOnAction(actionEvent -> deleteBank());
        deleteButton.setTooltip(new Tooltip("Delete " + bank.getBankName()));
        newButton.setOnAction(actionEvent -> showNewBranchDialog());
        newButton.setTooltip(new Tooltip("Create a new branch"));
        backButton.setOnAction(actionEvent -> showBanks());
        backButton.setTooltip(new Tooltip("Reselect bank"));
        editButton.setOnAction(actionEvent -> editBank());
        editButton.setTooltip(new Tooltip("Edit " + bank.getBankName()));
        mainPane.setCenter(createBranchPane(bank));
        statusLabel.setText("Choose a Branch!");
    }

    /**
     * Creates a <code>Pane</code> object to present the user all the <code>Branch</code> objects
     * associated with the selected bank from the <code>bank</code> parameter.
     * <br><br>
     * The pane consists of a <code>VBox</code> as a header to display the selected bank's details.
     * The rest will be a <code>FlowPane</code> fitted inside a <code>ScrollPane</code>, filled with
     * <code>Button</code> objects, where each button represents a branch.
     * <br><br>
     * Clicking on a button will bring the user to a new pane, where the user is provided with options
     * to manage the selected branch.
     *
     * @param bank the user's selected <code>Bank</code> object
     * @return the contents described above in a <code>FlowPane</code>
     *
     * @see FlowPane
     * @see VBox
     * @see ScrollPane
     * @see Branch
     */
    private Pane createBranchPane(Bank bank) {
        FlowPane pane = new FlowPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setHgap(10);
        pane.setVgap(10);

        VBox header = new VBox(10);
        header.setMinWidth(850);
        Label branchName = new Label("Managing " + bank.getBankName() + " [" + bank.getBankId() + "]");
        branchName.setFont(Font.font("Verdana", FontWeight.LIGHT, 30));
        Label instruction = new Label("Choose a branch to begin, or create a new one!");
        instruction.setFont(Font.font("Verdana", FontWeight.EXTRA_LIGHT, 15));
        header.getChildren().addAll(branchName, instruction, editButton);

        Separator separator = new Separator();
        separator.setMinWidth(830);
        pane.getChildren().addAll(header, separator);

        if (bank.getNumberOfBranches() == 0) {
            statusLabel.setText("Add a new branch to begin!");
            pane.getChildren().add(createEmptyPane());
        } else {
            FlowPane buttons = new FlowPane();
            buttons.setHgap(10);
            buttons.setVgap(10);
            buttons.setMinWidth(830);

            for (Branch branch : bank) {
                Button button = new Button(branch.getBranchName() + " [" + branch.getBranchCode() + "]");
                button.setPrefSize(195, 50);
                button.setOnAction(actionEvent -> showCustomers(bank.getBranch(branch.getBranchCode())));
                button.setTooltip(new Tooltip("Customers registered: " + branch.getNumberOfCustomers()));
                buttons.getChildren().add(button);
            }
            pane.getChildren().add(buttons);
        }

        return pane;
    }

    /**
     * Prepares the GUI to perform <code>Customer</code>-related tasks.
     * <br><br>
     * Will present a pane consisting of all the customers associated with <code>branch</code>,
     * displayed using <code>Button</code>s, and set the <code>newButton</code> to create
     * a new <code>Customer</code> object.
     * <br><br>
     * The <code>backButton</code> and <code>deleteButton</code> will be enabled here,
     * used for handling reselection and bank removal respectively.
     *
     * @param branch the selected <code>Branch</code> object
     *
     * @see Customer
     */
    private void showCustomers(Branch branch) {
        selectedBranch = branch;
        newButton.setOnAction(actionEvent -> showNewCustomerDialog());
        newButton.setTooltip(new Tooltip("Add a new customer"));
        deleteButton.setOnAction(actionEvent -> deleteBranch());
        deleteButton.setTooltip(new Tooltip("Delete " + branch.getBranchName()));
        backButton.setOnAction(actionEvent -> showBranches(selectedBank));
        backButton.setTooltip(new Tooltip("Reselect branch"));
        editButton.setOnAction(actionEvent -> editBranch());
        editButton.setTooltip(new Tooltip("Edit " + branch.getBranchName()));
        mainPane.setCenter(createCustomerPane(branch));
        statusLabel.setText("Select a Customer!");
    }

    /**
     * Creates a <code>Pane</code> object to present the user all the <code>Customer</code> objects
     * associated with the selected branch from the <code>branch</code> parameter.
     * <br><br>
     * The pane consists of a <code>VBox</code> as a header to display the selected branch's details.
     * The rest will be a <code>FlowPane</code> fitted inside a <code>ScrollPane</code>, filled with
     * <code>Button</code> objects, where each button represents a branch.
     * <br><br>
     * Clicking on a button will bring the user to a new pane, where the user is provided with options
     * to manage the selected customer.
     *
     * @param branch the user's selected <code>Branch</code> object
     * @return the contents described above in a <code>FlowPane</code>
     *
     * @see FlowPane
     * @see VBox
     * @see ScrollPane
     * @see Customer
     */
    private Pane createCustomerPane(Branch branch) {
        FlowPane pane = new FlowPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setHgap(10);
        pane.setVgap(10);

        VBox header = new VBox(10);
        header.setMinWidth(850);
        Label branchName = new Label("Welcome to " + branch.getBranchName());
        branchName.setFont(Font.font("Verdana", FontWeight.LIGHT, 30));
        Label branchAddress = new Label(branch.getAddressString());
        branchAddress.setFont(Font.font("Verdana", FontWeight.EXTRA_LIGHT, 15));
        header.getChildren().addAll(branchName, branchAddress, editButton);

        Separator separator = new Separator();
        separator.setMinWidth(830);
        pane.getChildren().addAll(header, separator);

        if (branch.getNumberOfCustomers() == 0) {
            statusLabel.setText("Add a new customer to begin!");
            pane.getChildren().add(createEmptyPane());
        } else {
            FlowPane buttons = new FlowPane();
            buttons.setHgap(10);
            buttons.setVgap(10);

            for (Customer customer : branch) {
                Button button = new Button(customer.getName() + " [" + customer.getCustomerId() + "]");
                button.setPrefSize(195, 50);
                button.setOnAction(actionEvent -> showAccounts(branch.getCustomer(customer.getCustomerId())));
                button.setTooltip(new Tooltip("Accounts open: " + customer.getNumberOfAccounts()));
                pane.getChildren().add(button);
            }

            pane.getChildren().add(buttons);
        }
        return pane;
    }

    /**
     * Prepares the GUI to perform <code>Account</code>-related tasks.
     * <br><br>
     * Will present a pane consisting of all the accounts opened by <code>customer</code>,
     * displayed using <code>Button</code>s, and set the <code>newButton</code> to open
     * a new <code>Account</code> object.
     * <br><br>
     * The <code>backButton</code> and <code>deleteButton</code> will be enabled here,
     * used for handling reselection and bank removal respectively.
     *
     * @param customer the selected <code>Customer</code> object
     *
     * @see Account
     */
    private void showAccounts(Customer customer) {
        selectedCustomer = customer;
        newButton.setOnAction(actionEvent -> showNewAccountDialog());
        newButton.setTooltip(new Tooltip("Open a new account"));
        deleteButton.setVisible(true);
        deleteButton.setOnAction(actionEvent -> deleteCustomer());
        deleteButton.setTooltip(new Tooltip("Remove " + customer.getName()));
        backButton.setOnAction(actionEvent -> showCustomers(selectedBranch));
        backButton.setTooltip(new Tooltip("Reselect customer"));
        mainPane.setCenter(createAccountsPane(customer));
        editButton.setOnAction(actionEvent -> editCustomer());
        editButton.setTooltip(new Tooltip("Edit your details"));
        statusLabel.setText("Hi " + customer.getName() + "!");
    }

    /**
     * Creates a <code>Pane</code> object to present the user all the <code>Account</code> objects
     * associated with the selected customer from the <code>customer</code> parameter.
     * <br><br>
     * The pane consists of a <code>VBox</code> as a header to display the selected customer's details.
     * The rest will be <code>Button</code> and <code>Label</code> objects to display the selected
     * customer's available <code>Account</code> objects.
     * <br><br>
     * Clicking on a button will bring the user to a new pane, where the user is provided with options
     * to manage the selected account.
     *
     * @param customer the user's selected <code>Customer</code> object
     * @return the contents described above in a <code>GridPane</code>
     *
     * @see GridPane
     * @see VBox
     * @see Account
     */
    private Pane createAccountsPane(Customer customer) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setPrefHeight(200);
        pane.setVgap(20);
        pane.setHgap(30);

        VBox greetings = new VBox(10);
        Label name = new Label("Hello " + customer.getName());
        name.setFont(Font.font("Verdana", FontWeight.LIGHT, 30));
        Label address = new Label(customer.getAddressString());
        address.setFont(Font.font("Verdana", FontWeight.EXTRA_LIGHT, 15));
        greetings.getChildren().addAll(name, address, editButton);
        pane.add(greetings, 0, 0);

        pane.add(new Separator(), 0, 1, 2, 1);

        if (customer.getNumberOfAccounts() == 0) {
            statusLabel.setText("Open a new customer to begin!");
            pane.add(new Label("Nothing to see here"), 0, 2, 2, 1);
        } else {
            int i = 2;
            for (Account account : customer) {
                Button button = new Button(account.getAccountNumber() + "");
                button.setPrefSize(200, 50);
                button.setOnAction(actionEvent -> showAccount(customer.getAccount(account.getAccountNumber())));
                button.setTooltip(new Tooltip("Open this account"));
                pane.add(button, 0, i);
                Label balance = new Label("Balance: " + currency.format(account.getBalance()));
                balance.setFont(new Font("Arial", 15));
                pane.add(balance, i - (i - 1), i);
                i++;
            }
        }
        pane.setAlignment(Pos.CENTER);
        return pane;
    }

    /**
     * Prepares the GUI to manage the selected <code>Account</code> object.
     * <br><br>
     * Will present a pane with buttons that allow the user to manage their account's
     * funds.
     * <br><br>
     * The <code>backButton</code> will be enabled, but not the <code>deleteButton</code>,
     * since that functionality will be for another button within the pane.
     *
     * @param account the selected <code>Account</code> object
     */
    private void showAccount(Account account) {
        selectedAccount = account;
        deleteButton.setVisible(false);
        backButton.setOnAction(actionEvent -> showAccounts(selectedCustomer));
        backButton.setTooltip(new Tooltip("Reselect account"));
        mainPane.setCenter(createAccountPane(account));
        statusLabel.setText("Managing Account #" + account.getAccountNumber());
    }

    /**
     * Creates a <code>Pane</code> object to present the user all options for managing their selected
     * account.
     * <br><br>
     * The pane consists of a <code>VBox</code> as a header to display the selected account's details.
     * The rest will be four <code>Button</code>s fitted into a <code>GridPane</code>, allowing the user
     * to deposit funds into the account, withdraw funds from the account, transfer funds to another
     * customer account, and to close the account (which will remove it from the database as well).
     * <br><br>
     * Clicking on a button will present a <code>Dialog</code>, which will serve whatever purpose
     * the <code>Button</code> displays.
     *
     * @param account the user's selected <code>Account</code> object
     * @return the contents described above in a <code>VBox</code>
     *
     * @see VBox
     * @see GridPane
     * @see Dialog
     */
    private Pane createAccountPane(Account account) {
        VBox pane = new VBox(20);
        pane.setAlignment(Pos.CENTER);

        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        Label accountNumber = new Label("Opening #" + account.getAccountNumber());
        accountNumber.setFont(Font.font("Verdana", FontWeight.LIGHT, 25));
        Label balance = new Label("Balance: " + currency.format(account.getBalance()));
        balance.setFont(Font.font("Verdana", FontWeight.EXTRA_LIGHT, 20));
        header.getChildren().addAll(accountNumber, balance);

        Label options = new Label("What would you like to do today?");
        options.setFont(Font.font("Verdana", FontWeight.EXTRA_LIGHT, 15));

        GridPane buttons = new GridPane();
        buttons.setHgap(10);
        buttons.setVgap(10);
        buttons.setAlignment(Pos.CENTER);

        Button deposit = new Button("Deposit");
        deposit.setMinSize(100, 30);
        deposit.setTooltip(new Tooltip("Add money into your account"));
        deposit.setOnAction(actionEvent -> showDepositDialog());

        Button withdraw = new Button("Withdraw");
        withdraw.setMinSize(100, 30);
        withdraw.setTooltip(new Tooltip("Take money from your account"));
        withdraw.setOnAction(actionEvent -> showWithdrawDialog());

        Button transfer = new Button("Transfer");
        transfer.setMinSize(100, 30);
        transfer.setTooltip(new Tooltip("Transfer your money to another customer"));
        transfer.setOnAction(actionEvent -> showTransferDialog());

        Button close = new Button("Close Account");
        close.setMinSize(320, 30);
        close.setTooltip(new Tooltip("Withdraw all funds and close the account"));
        close.setOnAction(actionEvent -> showCloseAccountDialog());

        buttons.add(deposit, 0, 0);
        buttons.add(withdraw, 1, 0);
        buttons.add(transfer, 2, 0);
        buttons.add(close, 0, 1, 3, 1);

        pane.getChildren().addAll(header, new Separator(), options, buttons);

        return pane;
    }

    /**
     * Presents an <code>Alert</code> of type <code>CONFIRMATION</code> to verify that
     * the user wishes to delete a bank.
     * <br><br>
     * This method is typically called from the <code>deleteButton</code>.
     *
     * @see Alert
     * @see Bank
     */
    private void deleteBank() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting " + selectedBank.getBankName());
        alert.setHeaderText("Are you sure you want to delete " + selectedBank.simplifiedString());
        alert.setContentText("This action is undoable. All related data will be deleted forever.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            statusLabel.setText("Deleted " + selectedBank.simplifiedString() + " from database.");
            manager.removeBank(selectedBank.getBankId());
            showBanks();
        }
    }

    /**
     * Presents an <code>Alert</code> of type <code>CONFIRMATION</code> to verify that
     * the user wishes to delete a branch.
     * <br><br>
     * This method is typically called from the <code>deleteButton</code>.
     *
     * @see Alert
     * @see Branch
     */
    private void deleteBranch() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting " + selectedBranch.simplifiedString());
        alert.setHeaderText("Are you sure you want to delete " + selectedBranch.simplifiedString());
        alert.setContentText("This action is undoable. All related data will be deleted forever.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            statusLabel.setText("Deleted " + selectedBranch.simplifiedString() + " from database.");
            selectedBank.removeBranch(selectedBranch.getBranchCode());
            showBranches(selectedBank);
        }
    }

    /**
     * Presents an <code>Alert</code> of type <code>CONFIRMATION</code> to verify that
     * the user wishes to remove a customer.
     * <br><br>
     * This method is typically called from the <code>deleteButton</code>.
     *
     * @see Alert
     * @see Customer
     */
    private void deleteCustomer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Removing " + selectedCustomer.simplifiedString());
        alert.setHeaderText("Are you sure you want to delete " + selectedCustomer.simplifiedString());
        alert.setContentText("This action is undoable. All related data will be deleted forever.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            statusLabel.setText(selectedCustomer.simplifiedString() + " has been removed.");
            selectedBranch.removeCustomer(selectedCustomer.getCustomerId());
            showCustomers(selectedBranch);
        }
    }

    /**
     * Presents a <code>Dialog</code> to the user that allows the creation of a new <code>Bank</code>.
     * <br><br>
     * Typically called from <code>newButton</code> at the application home screen.
     * <br><br>
     * This dialog is loaded from the <code>bankDialog.fxml</code> file, and it is only used to retrieve
     * a name for the bank.
     * <br><br>
     * The name will then be used to check whether there are existing banks with the same name. If a duplicate
     * is found, an <code>Alert</code> will be shown, instructing the user to select another name.
     *
     * @see Dialog
     * @see Alert
     * @see Bank
     */
    private void showNewBankDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Create a new Bank");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("bankDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            showDialogError();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        while (true) {
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.FINISH)) {
                BankDialogController controller = loader.getController();
                String bankName = controller.processResults();
                if (bankName.equals("")) {
                    bankName = "The Bank without a name";
                }
                if (manager.createBank(bankName)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful!");
                    alert.setHeaderText("Added new Bank: " + bankName);
                    alert.setContentText("You can now manage your new bank by clicking on the button with your bank name");
                    alert.show();
                    showBanks();
                    statusLabel.setText("You just added a new bank! Go ahead and manage it.");
                    break;
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Duplicate Found");
                    alert.setContentText("A Bank with the name: " + bankName + " already exists.");
                    alert.showAndWait();
                }
            }
        }
    }

    /**
     * Presents a <code>Dialog</code> to the user that allows the creation of a new <code>Branch</code>.
     * <br><br>
     * Typically called from <code>newButton</code> when managing the selected bank.
     * <br><br>
     * This dialog is loaded from the <code>withAddressDialog.fxml</code> file, and it is used to retrieve
     * a name for the branch, and its address.
     * <br><br>
     * The name will then be used to check whether there are existing branches with the same name. If a duplicate
     * is found, an <code>Alert</code> will be shown, instructing the user to select another name. This will also
     * happen if the entered address information is incomplete.
     *
     * @see Dialog
     * @see Alert
     * @see Branch
     * @see Address
     */
    private void showNewBranchDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Create a new Branch");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("withAddressDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        while (true) {
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.FINISH)) {
                WithAddressController controller = loader.getController();
                String[] results = controller.processResults();
                if (results == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Address");
                    alert.setContentText("Please ensure you properly all information correctly and try again.");
                    alert.showAndWait();
                } else if (selectedBank.createBranch(results[0], new Address(results[1], results[2], results[3], results[4]))) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful!");
                    alert.setHeaderText("Added new Branch: " + results[0]);
                    alert.setContentText("You can now manage your new branch by clicking on the button with your branch name");
                    alert.show();
                    showBranches(selectedBank);
                    statusLabel.setText("You just added a new branch! Go ahead and manage it.");
                    break;
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Application Error");
                    alert.setContentText("Failed to create branch. Please contact developer.");
                    alert.show();
                    break;
                }
            } else {
                break;
            }
        }
    }

    /**
     * Presents a <code>Dialog</code> to the user that allows the creation of a new <code>Customer</code>.
     * <br><br>
     * Typically called from <code>newButton</code> when managing the selected branch.
     * <br><br>
     * This dialog is loaded from the <code>withAddressDialog.fxml</code> file, and it is used to retrieve
     * a name for the customer, and his/her address.
     * <br><br>
     * Duplicate customer names will be accepted, since they have their IDs distinguishing them, and the possibility
     * of two customers have the same name is likely. However, if the entered address information is incomplete, an
     * <code>Alert</code> will be shown, instructing the user to enter proper information.
     * happen
     *
     * @see Dialog
     * @see Alert
     * @see Customer
     * @see Address
     */
    private void showNewCustomerDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Add a new Customer");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("withAddressDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            showDialogError();
            e.printStackTrace();
        }
        WithAddressController controller = loader.getController();
        controller.handleCustomers();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        while (true) {
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.FINISH)) {
                String[] results = controller.processResults();
                if (results == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Address");
                    alert.setContentText("Please ensure you properly entered address information and try again.");
                    alert.showAndWait();
                } else if (selectedBranch.addCustomer(results[0], new Address(results[1], results[2], results[3], results[4]))) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful!");
                    alert.setHeaderText("Added new Customer: " + results[0]);
                    alert.setContentText("You can now manage the customer accounts by clicking on the button with the " +
                            "customer name");
                    alert.show();
                    showCustomers(selectedBranch);
                    statusLabel.setText("You just added a new customer! Go ahead and manage it.");
                    break;
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Application Error");
                    alert.setContentText("Failed to add customer. Please contact developer.");
                    alert.show();
                    break;
                }
            } else {
                break;
            }
        }
    }

    /**
     * Allows the user to open a new <code>Account</code>.
     * <br><br>
     * Typically called from <code>newButton</code> when managing the selected customer.
     * <br><br>
     * If the new account is successfully opened, then an <code>Alert</code> will be shown to notify
     * the user. However, the <code>Customer</code> class limits the maximum number of accounts open
     * to just five (5) accounts. When the limit is met, the <code>Alert</code> will notify that the user
     * can no more open new accounts.
     *
     * @see Alert
     * @see Account
     * @see Customer
     */
    private void showNewAccountDialog() {
        Account account = selectedCustomer.openAccount();
        if (account != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Account Opened");
            alert.setHeaderText("Account Opening Successful!");
            alert.setContentText("Account #" + account.getAccountNumber() + " opened.");
            alert.show();
            showAccounts(selectedCustomer);
            statusLabel.setText("You just opened a new account. Go ahead and manage it!");
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Maximum number of accounts opened");
            alert.setContentText("You have the maximum number of accounts opened (" +
                    selectedCustomer.getNumberOfAccounts() + ").");
            alert.show();
        }
    }

    /**
     * Opens a <code>Dialog</code> that allows the user to deposit funds into the selected <code>Account</code>.
     * <br><br>
     * The dialog will be loaded from the <code>amountDialog.fxml</code> file, and will retrieve a
     * <code>Double</code> value as the desired amount of money to be deposited.
     * <br><br>
     * Any value above zero (0) will be accepted. Otherwise, an <code>Alert</code> will be shown
     * detailing the reason why.
     *
     * @see Dialog
     * @see Alert
     * @see Account
     */
    private void showDepositDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Depositing money to account #" + selectedAccount.getAccountNumber() + ".");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("amountDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            showDialogError();
            e.printStackTrace();
        }
        AmountDialogController controller = loader.getController();
        controller.setBalance(selectedAccount.getBalance());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Alert cancel = new Alert(Alert.AlertType.WARNING);
        cancel.setTitle("No money deposited");
        cancel.setContentText("Deposit cancelled");

        while (true) {
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                double amount = controller.processResults();
                if (amount <= 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No money deposited");
                    alert.setHeaderText("No money deposited into account.");
                    alert.setContentText("Amount entered was $0.00.");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm deposit");
                    alert.setHeaderText("Are you sure you want to deposit the following amount of money?");
                    alert.setContentText("Entered amount: " + currency.format(amount));
                    Optional<ButtonType> confirm = alert.showAndWait();
                    if (confirm.isPresent() && confirm.get().equals(ButtonType.OK)) {
                        selectedAccount.deposit(amount);
                        Alert information = new Alert(Alert.AlertType.INFORMATION);
                        information.setTitle("Deposit Successful");
                        information.setHeaderText("Deposited " + currency.format(amount));
                        information.setContentText("Account balance is now " + currency.format(selectedAccount.getBalance()));
                        information.show();
                        showAccount(selectedAccount);
                        statusLabel.setText("You just deposited " + currency.format(amount) +
                                " to account #" + selectedAccount.getAccountNumber() + ".");
                    } else {
                        cancel.show();
                    }
                    break;
                }
            } else {
                cancel.show();
                break;
            }
        }
    }

    /**
     * Opens a <code>Dialog</code> that allows the user to withdraw funds from the selected <code>Account</code>.
     * <br><br>
     * The dialog will be loaded from the <code>amountDialog.fxml</code> file, and will retrieve a
     * <code>Double</code> value as the desired amount of money to be withdrawn.
     * <br><br>
     * Any value above zero (0) and below the account's balance will be accepted. Otherwise, an
     * <code>Alert</code> will be shown detailing the reason why.
     *
     * @see Dialog
     * @see Alert
     * @see Account
     */
    private void showWithdrawDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Withdrawing money from account #" + selectedAccount.getAccountNumber() + ".");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("amountDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            showDialogError();
            e.printStackTrace();
        }
        AmountDialogController controller = loader.getController();
        controller.setBalance(selectedAccount.getBalance());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Alert cancel = new Alert(Alert.AlertType.WARNING);
        cancel.setTitle("No money withdrawn");
        cancel.setContentText("Withdrawal cancelled");

        while (true) {
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                double amount = controller.processResults();
                if (amount <= 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No money withdrawn");
                    alert.setHeaderText("No money withdrawn from account.");
                    alert.setContentText("Amount entered was $0.00.");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm deposit");
                    alert.setHeaderText("Are you sure you want to withdraw the following amount of money?");
                    alert.setContentText("Entered amount: " + currency.format(amount));
                    Optional<ButtonType> confirm = alert.showAndWait();
                    if (confirm.isPresent() && confirm.get().equals(ButtonType.OK)) {
                        if (selectedAccount.withdraw(amount)) {
                            Alert information = new Alert(Alert.AlertType.INFORMATION);
                            information.setTitle("Withdrawal Successful");
                            information.setHeaderText("Withdrew " + currency.format(amount));
                            information.setContentText("Account balance is now " + currency.format(selectedAccount.getBalance()));
                            information.show();
                            showAccount(selectedAccount);
                            statusLabel.setText("You just withdrew " + currency.format(amount) +
                                    " from account #" + selectedAccount.getAccountNumber() + ".");
                            break;
                        } else {
                            Alert information = new Alert(Alert.AlertType.WARNING);
                            information.setTitle("Withdrawal Unsuccessful");
                            information.setHeaderText("Insufficient Balance");
                            information.setContentText("Account balance is " + currency.format(selectedAccount.getBalance()) +
                                    "\nEntered amount: " + currency.format(amount) +
                                    " [+" + currency.format(amount - selectedAccount.getBalance()) + "]");
                            information.showAndWait();
                        }
                    } else {
                        cancel.show();
                        break;
                    }
                }
            } else {
                cancel.show();
                break;
            }
        }
    }

    /**
     * Opens a <code>Dialog</code> that allows the user to transfer funds from the selected <code>Account</code>
     * to another customer's <code>Account</code>.
     * <br><br>
     * A dialog will first be loaded from the <code>customersDialog.fxml</code> to display a list of
     * customers registered within the selected branch.
     * <br><br>
     * Once the customer has selected a recipient, a dialog will be loaded from the <code>amountDialog.fxml</code>
     * file, and will retrieve a <code>Double</code> value as the desired amount of money to be transferred.
     * <br><br>
     * Any value above zero (0) and below the account's balance will be accepted. Otherwise, an
     * <code>Alert</code> will be shown detailing the reason why.
     * <br><br>
     * If there are no other customers within the branch, the method will only show an <code>Alert</code>
     * notifying the user, and will do nothing else at all.
     *
     * @see Dialog
     * @see Alert
     * @see Customer
     * @see Account
     */
    private void showTransferDialog() {
        if (selectedBranch.getNumberOfCustomers() == 1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No customers available");
            alert.setHeaderText("No other customers available");
            alert.setContentText("You are the only customer within the " + selectedBranch.getBranchName() + " branch.");
            alert.show();
            return;
        }
        Alert cancel = new Alert(Alert.AlertType.WARNING);
        cancel.setTitle("No money transferred");
        cancel.setContentText("Transfer cancelled");

        //Customer Dialog
        Dialog<ButtonType> customerDialog = new Dialog<>();
        customerDialog.initOwner(mainPane.getScene().getWindow());
        customerDialog.setTitle("Select a customer.");
        FXMLLoader customerLoader = new FXMLLoader(getClass().getResource("customersDialog.fxml"));
        try {
            customerDialog.getDialogPane().setContent(customerLoader.load());
        } catch (IOException e) {
            showDialogError();
            System.out.println("Customer Dialog Error");
            e.printStackTrace();
        }
        CustomersDialogController customerController = customerLoader.getController();
        customerController.populateCustomerListView(selectedBank, selectedCustomer);
        customerDialog.getDialogPane().getButtonTypes().add(ButtonType.NEXT);
        customerDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        //Account Dialog
        Dialog<ButtonType> accountDialog = new Dialog<>();
        accountDialog.initOwner(mainPane.getScene().getWindow());
        accountDialog.setTitle("Select an account.");
        FXMLLoader accountLoader = new FXMLLoader(getClass().getResource("accountsDialog.fxml"));

        try {
            accountDialog.getDialogPane().setContent(accountLoader.load());
        } catch (IOException e) {
            showDialogError();
            System.out.println("Account Dialog Error");
            e.printStackTrace();
        }
        AccountsDialogController accountController = accountLoader.getController();
        accountDialog.getDialogPane().getButtonTypes().add(ButtonType.NEXT);
        accountDialog.getDialogPane().getButtonTypes().add(ButtonType.PREVIOUS);

        //Amount Dialog
        Dialog<ButtonType> amountDialog = new Dialog<>();
        amountDialog.initOwner(mainPane.getScene().getWindow());
        amountDialog.setTitle("Transferring money from account #" + selectedAccount.getAccountNumber() + ".");
        FXMLLoader amountLoader = new FXMLLoader(getClass().getResource("amountDialog.fxml"));
        try {
            amountDialog.getDialogPane().setContent(amountLoader.load());
        } catch (IOException e) {
            showDialogError();
            System.out.println("Amount Dialog error");
            e.printStackTrace();
        }
        AmountDialogController amountController = amountLoader.getController();
        amountController.setBalance(selectedAccount.getBalance());
        amountDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        amountDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        boolean notDone = true;

        while (notDone) {
            Optional<ButtonType> customerResult = customerDialog.showAndWait();
            if (customerResult.isPresent() && customerResult.get().equals(ButtonType.NEXT)) {
                Customer recipient = customerController.processResults();
                if (recipient == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("No customer selected");
                    alert.setContentText("Please select a customer to continue");
                } else {
                    accountController.populateAccountListView(recipient);
                    while (notDone) {
                        Optional<ButtonType> accountResult = accountDialog.showAndWait();
                        if (accountResult.isPresent() && accountResult.get().equals(ButtonType.NEXT)) {
                            Account recipientAccount = accountController.processResults();
                            if (recipientAccount == null) {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("No account selected");
                                alert.setContentText("Please select an account to continue");
                            } else {
                                while (notDone) {
                                    Optional<ButtonType> amountResult = amountDialog.showAndWait();
                                    if (amountResult.isPresent() && amountResult.get().equals(ButtonType.OK)) {
                                        double amount = amountController.processResults();
                                        if (amount <= 0) {
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                            alert.setTitle("Please re-enter amount");
                                            alert.setHeaderText("No money transferred from account.");
                                            alert.setContentText("Amount entered was $0.00.");
                                            alert.showAndWait();
                                        } else {
                                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                            alert.setTitle("Confirm Transfer");
                                            alert.setHeaderText("Are you sure you want to transfer the following amount of money?");
                                            alert.setContentText("Entered amount: " + currency.format(amount)
                                                    + "\nTo: " + recipient.simplifiedString() + " @ account #" +
                                                    recipientAccount.getAccountNumber());
                                            Optional<ButtonType> confirm = alert.showAndWait();
                                            if (confirm.isPresent() && confirm.get().equals(ButtonType.OK)) {
                                                if (selectedAccount.transfer(recipientAccount, amount)) {
                                                    Alert information = new Alert(Alert.AlertType.INFORMATION);
                                                    information.setTitle("Transfer Successful");
                                                    information.setHeaderText("Transferred " + currency.format(amount));
                                                    information.setContentText("Account balance is now " + currency.format(selectedAccount.getBalance()));
                                                    information.show();
                                                    showAccount(selectedAccount);
                                                    statusLabel.setText("You just transferred " + currency.format(amount) +
                                                            " to " + recipient.getName() + ".");
                                                    notDone = false;
                                                } else {
                                                    Alert information = new Alert(Alert.AlertType.WARNING);
                                                    information.setTitle("Transfer Unsuccessful");
                                                    information.setHeaderText("Insufficient Balance. Please re-enter amount");
                                                    information.setContentText("Account balance is " + currency.format(selectedAccount.getBalance()) +
                                                            "\nEntered amount: " + currency.format(amount) +
                                                            " [+" + currency.format(amount - selectedAccount.getBalance()) + "]");
                                                    information.showAndWait();
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            break;
                        }
                    }
                }
            } else {
                cancel.show();
                break;
            }
        }
    }

    /**
     * Shows an <code>Alert</code> to confirm the user that the selected <code>Account</code>
     * will be deleted. If the user confirms, then the account will be removed from the
     * customer's database.
     *
     * @see Alert
     * @see Account
     */
    private void showCloseAccountDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close Account?");
        alert.setHeaderText("Are you sure you want to close account #" + selectedAccount.getAccountNumber());
        alert.setContentText(selectedAccount.getBalance() != 0 ?
                "Existing funds of " + currency.format(selectedAccount.getBalance()) + " will be withdrawn" : "");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            statusLabel.setText("You have closed account #" + selectedAccount.getAccountNumber());
            if (selectedAccount.getBalance() != 0) {
                selectedAccount.withdraw(selectedAccount.getBalance());
            }
            selectedCustomer.closeAccount(selectedAccount);
            Alert done = new Alert(Alert.AlertType.INFORMATION);
            done.setTitle("Account closed");
            done.setHeaderText("Account has been closed.");
            done.show();
            showAccounts(selectedCustomer);
        }
    }

    /**
     * Opens a <code>Dialog</code> that allows the user to edit the selected <code>Bank</code>'s
     * details. The dialog will be loaded from the file <code>bankDialog.fxml</code>, and used to
     * retrieve the bank's new name.
     * <br><br>
     * Typically called by the <code>editButton</code> when managing the selected bank.
     *
     * @see Dialog
     * @see Bank
     */
    private void editBank() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Edit " + selectedBank.getBankName());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("bankDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            showDialogError();
            return;
        }
        BankDialogController controller = loader.getController();
        controller.editMode(selectedBank);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        while (true) {
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.FINISH)) {
                String bankName = controller.processResults();
                if (bankName.equals("")) {
                    bankName = "The Bank without a name";
                }
                boolean duplicate = false;
                for (Bank bank : manager) {
                    if (bank.getBankName().equals(bankName)) {
                        duplicate = true;
                        break;
                    }
                }
                if (duplicate) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Duplicate Found");
                    alert.setContentText("A Bank with the name: " + bankName + " already exists.");
                    alert.showAndWait();
                } else {
                    selectedBank.setBankName(bankName);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful!");
                    alert.setHeaderText("Edited Bank Name: " + bankName);
                    alert.setContentText("Bank name change successful");
                    alert.show();
                    showBranches(selectedBank);
                    statusLabel.setText("You just edited a bank name (" + bankName + ").");
                    break;
                }
            } else {
                break;
            }
        }
    }

    /**
     * Opens a <code>Dialog</code> that allows the user to edit the selected <code>Branch</code>'s
     * details. The dialog will be loaded from the file <code>withAddressDialog.fxml</code>, and used to
     * retrieve the branch's new name and address.
     * <br><br>
     * Typically called by the <code>editButton</code> when managing the selected branch.
     *
     * @see Dialog
     * @see Branch
     * @see Address
     */
    private void editBranch() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Edit " + selectedBranch.getBranchName());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("withAddressDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            showDialogError();
            return;
        }
        WithAddressController controller = loader.getController();
        controller.editMode(selectedBranch);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        while (true) {
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.FINISH)) {
                String[] results = controller.processResults();
                boolean duplicate = false;
                if (results == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Error");
                    alert.setContentText("Incomplete information. Please enter all fields correctly.");
                    alert.showAndWait();
                } else {
                    if (!results[0].equals(selectedBranch.getBranchName())) {
                        for (Branch branch : selectedBank) {
                            if (branch.getBranchName().equals(results[0])) {
                                duplicate = true;
                                break;
                            }
                        }
                    }
                    if (duplicate) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Duplicate Found");
                        alert.setContentText("A branch with the name: " + results[0] + " already exists.");
                        alert.showAndWait();
                    } else {
                        selectedBranch.setBranchName(results[0]);
                        selectedBranch.setAddress(new Address(results[1], results[2], results[3], results[4]));
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Successful!");
                        alert.setHeaderText("Edited Branch Details");
                        alert.setContentText("Branch details change successful");
                        alert.show();
                        showCustomers(selectedBranch);
                        statusLabel.setText("You just edited a branch's details.");
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    /**
     * Opens a <code>Dialog</code> that allows the user to edit the selected <code>Customer</code>'s
     * details. The dialog will be loaded from the file <code>withAddressDialog.fxml</code>, and used to
     * retrieve the customer's new name and address.
     * <br><br>
     * Typically called by the <code>editButton</code> when managing the selected customer.
     *
     * @see Dialog
     * @see Customer
     * @see Address
     */
    private void editCustomer() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Edit " + selectedCustomer.getName());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("withAddressDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            showDialogError();
            return;
        }
        WithAddressController controller = loader.getController();
        controller.editMode(selectedCustomer);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        while (true) {
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.FINISH)) {
                String[] results = controller.processResults();
                if (results == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Error");
                    alert.setContentText("Incomplete information. Please enter all fields correctly.");
                    alert.showAndWait();
                } else {
                    selectedCustomer.setName(results[0]);
                    selectedCustomer.setAddress(new Address(results[1], results[2], results[3], results[4]));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful!");
                    alert.setHeaderText("Edited Customer Details");
                    alert.setContentText("Customer details change successful");
                    alert.show();
                    showAccounts(selectedCustomer);
                    statusLabel.setText("You just edited a customer's details.");
                    break;
                }
            } else {
                break;
            }
        }
    }

    /**
     * Creates an <code>Alert</code> to show that an application error has occurred.
     *
     * @see Alert
     */
    private void showDialogError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Application Error");
        alert.setContentText("Failed to load dialog. Please contact developer.");
        alert.show();
    }

    /**
     * Creates a <code>ScrollPane</code> used to fit a pane specified by the <code>content</code>
     * parameter.
     * <br><br>
     * The <code>ScrollPane</code> will only have a vertical scroll bar whenever the contents are
     * exceeding its bounds. The horizontal scroll bar is locked, and cannot be used.
     *
     * @param content the pane to be fitted inside the <code>ScrollPane</code>
     * @return a <code>ScrollPane</code> with the <code>content</code> pane fitted in it
     */
    private ScrollPane createScrollPane(Pane content) {
        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setMinWidth(830);
        scroll.setMaxHeight(400);
        scroll.setStyle("-fx-background-color:transparent;");
        scroll.setContent(content);
        scroll.viewportBoundsProperty().addListener((ov, oldBounds, bounds) -> {
            content.setPrefWidth(bounds.getWidth());
            content.setPrefHeight(bounds.getHeight());
        });
        return scroll;
    }

    /**
     * Handles the code for the application to exit. Only used by the <code>exitButton</code>.
     */
    @FXML
    public void handleExit() {
        Platform.exit();
    }

    /**
     * Calls the <code>save()</code> method from the <code>BankManager</code> class.
     * <br><br>
     * Only called when the user quits the application.
     */
    public void shutdown() {
        manager.save();
    }

}
