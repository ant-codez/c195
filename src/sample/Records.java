package sample;

import helper.Customer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.Set;

public class Records {

    ObservableList<Customer> customerList;
    private static String state = "Empty";

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button btn_add;
    @FXML
    private ComboBox<String> cb_country;
    @FXML
    private ComboBox<String> cb_state;
    @FXML
    private TableColumn<Customer, Integer> col_ID;
    @FXML
    private TableColumn<Customer, Integer> col_address;
    @FXML
    private TableColumn<Customer, Integer> col_division;
    @FXML
    private TableColumn<Customer, Integer> col_name;
    @FXML
    private TableColumn<Customer, Integer> col_phone;
    @FXML
    private TableColumn<Customer, Integer> col_postal;
    @FXML
    private Label label_title;
    @FXML
    private TableView<Customer> table;
    @FXML
    private TextField tf_address;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_phoneNumber;
    @FXML
    private TextField tf_postalCode;
    @FXML
    private TextField tf_ID;

    public static void setState(String _state) {
        state = _state;
    }

    /**
     * Updates the state based on the user's input and enables or disables the state combo box accordingly.
     */
    @FXML public void updateState() {
        System.out.println("CALL UPDATE STATE");

        if (cb_country.getSelectionModel().isEmpty() == false && !state.equals("Delete")) {
            cb_state.setDisable(false);
            cb_state.setItems(sample.Controller.getTableOfStates(cb_country.getSelectionModel().getSelectedItem()));
            System.out.println("Enabled Combo Box...");
        }
    }

    /**
     * Adds a new customer record to the database.
     * @throws SQLException if there is an error accessing the database
     */
    private void addRecord() throws SQLException {
        System.out.println("Adding Customer Data to DataBase...");
        String Name = tf_name.getText();
        String address = tf_address.getText();
        String phoneNumber = tf_phoneNumber.getText();
        String postalCode = tf_postalCode.getText();
        String state = cb_state.getSelectionModel().getSelectedItem();

        System.out.println(state);

        if (Name.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || postalCode.isEmpty() || state == null) {
            Main.alertError("Error Creating Customer, one or more fields have been left blank!!");
            return;
        }

        helper.Records.addCustomer(Name, address, phoneNumber, postalCode, Controller.statesHashMap.get(state));
        Main.alertSuccess("Successfully Created new Customer Record!!");
        initialize();
    }

    /**
     * Updates an existing customer record in the database.
     * @throws Exception if there is an error accessing the database
     */
    private void updateRecord() throws Exception {
        System.out.println("Updating Customer Data in DataBase...");

        String Name = tf_name.getText();
        String address = tf_address.getText();
        String phoneNumber = tf_phoneNumber.getText();
        String postalCode = tf_postalCode.getText();
        String state = cb_state.getSelectionModel().getSelectedItem();
        Integer userID = Integer.parseInt(tf_ID.getText());

        System.out.println(state);

        if (Name.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || postalCode.isEmpty() || state == null) {
            Main.alertError("Error Updating Customer, one or more fields have been left blank!!");
            return;
        }

        helper.Records.updateCustomerRecord(Name, address, phoneNumber, postalCode, Controller.statesHashMap.get(state), userID);
        Main.alertSuccess("Successfully Updated Customer Record!!");
        initialize();
    }

    /**
     * Deletes an existing customer record from the database.
     * @throws Exception if there is an error accessing the database
     */
    private void deleteRecord() throws Exception {
        System.out.println("Deleting Customer Data in DataBase...");
        Integer userID = Integer.parseInt(tf_ID.getText());

        if (!Main.alertConfirmation("Are you sure you want to delete this user??")) {
            System.out.println("Canceled Deleting User...");
            return;
        }

        //delete all Appointments associated with Customer
        helper.Records.deleteAppointments(userID);
        //delete customer from DB
        helper.Records.deleteCustomerRecord(userID);

        Main.alertSuccess("Successfully Deleted Customer Record!!");
        removeTextFromFields();
        initialize();
    }

    /**
     * Performs the appropriate action when the button is clicked.
     * @throws Exception if there is an error accessing the database
     */
    @FXML public void btnClick() throws Exception {
        if (state.equals("Delete")) {
            deleteRecord();
        }
        else if (state.equals("Update")) {
            updateRecord();
        }
        else if (state.equals("Add")) {
            addRecord();
        }
    }

    /**
     * Checks the current state and updates the UI elements accordingly.
     */
    private void checkState() {
        System.out.println("State = " + state);

        if (state.equals("Delete")) {
            disableElements();
            label_title.setText("Delete Record");
            btn_add.setText("Delete Record");
        }
        else if (state.equals("Update")) {
            label_title.setText("Update Record");
            btn_add.setText("Update Record");
        }
        else if (state.equals("Add")) {
            label_title.setText("Add Record");
            btn_add.setText("Add Record");
        }
    }

    /**
     * This function is triggered when the user clicks on a row in the table. It retrieves the selected Customer object from the TableView,
     * and populates the form fields with the customer's information if the application is in either "Update" or "Delete" state.
     */
    @FXML public void getMouseClick() {
        System.out.println("Getting mouse click");
        Customer selected = table.getSelectionModel().getSelectedItem();

        if (state.equals("Update") || state.equals("Delete")) {
            tf_name.setText(selected.getName());
            tf_address.setText(selected.getAddress());
            tf_phoneNumber.setText(selected.getPhoneNumber());
            tf_postalCode.setText(selected.getPostalCode());
            tf_ID.setText(Integer.toString(selected.getID()));
            cb_state.setValue(selected.getState_Name());
            cb_country.setValue(selected.getCountry_Name());

        }

    }

    /**
     * This function is executed when the FXML file is loaded. It sets up the TableView
     * to display Customer records, and populates the ComboBox with the available country options.
     * @throws SQLException if there is an error accessing the database
     */
    @FXML public void initialize() throws SQLException {
        System.out.println("TEST INIT");

        cb_country.setItems(sample.Controller.getTableOfCountrys());
        customerList = helper.Records.getCustomerRecords();
        checkState();

        table.setItems(customerList);
        col_name.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("name"));
        col_ID.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("ID"));
        col_address.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("address"));
        col_postal.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("postalCode"));
        col_division.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("division_ID"));
        col_phone.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("phoneNumber"));
    }

    /**
     * This function is called to disable all input elements in the AnchorPane.
     * It retrieves all the Node objects with the CSS class "input" and sets their disable property to true.
     */
    public void disableElements() {
        Set<Node> group =  anchorPane.lookupAll(".input");

        System.out.println(group.size());

        for (Node node : group) {
            node.setDisable(true);
        }
    }

    public void removeTextFromFields() {
        Set<Node> group =  anchorPane.lookupAll(".input");

        for (Node node : group) {
            if(node instanceof TextField){
                ((TextField) node).clear();
            }
            else if(node instanceof ComboBox){
                ((ComboBox) node).setValue("");
            }
        }

    }

    /**
     * This function is called when the user clicks the "Back" button in the UI. It switches the scene to the homepage.fxml file.
     * @throws Exception if there is an error accessing the file
     */
    @FXML public void back() throws Exception {
        Main.switchScene("/sample/homepage.fxml", Main.getStage());
    }

}
