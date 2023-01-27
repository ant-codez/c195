package sample;

import helper.Customer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.Vector;


public class Records {

    ObservableList<Customer> customerList;
    private static String state = "Empty";

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button btn_add;
    @FXML
    private Button btn_back;
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

    @FXML public void updateState() {
        System.out.println("CALL UPDATE STATE");

        if (cb_country.getSelectionModel().isEmpty() == false && !state.equals("Delete")) {
            cb_state.setDisable(false);
            cb_state.setItems(sample.Controller.getTableOfStates(cb_country.getSelectionModel().getSelectedItem()));
            System.out.println("Enabled Combo Box...");
        }
    }

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

    private void deleteRecord() throws Exception {
        System.out.println("Deleting Customer Data in DataBase...");
        Integer userID = Integer.parseInt(tf_ID.getText());

        if (!Main.alertConfirmation("Are you sure you want to delete this user??")) {
            System.out.println("Canceled Deleting User...");
            return;
        }

        helper.Records.deleteCustomerRecord(userID);

        Main.alertSuccess("Successfully Deleted Customer Record!!");
        removeTextFromFields();
        initialize();
    }

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

    @FXML public void back() throws Exception {
        Main.switchScene("/sample/homepage.fxml", Main.getStage());
    }

}
