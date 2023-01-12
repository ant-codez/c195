package sample;

import helper.Customer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.Vector;


public class Records {
    ObservableList<Customer> customerList;
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

    @FXML public void updateState() {
        System.out.println("CALL UPDATE STATE");

        if (cb_country.getSelectionModel().isEmpty() == false) {
            cb_state.setDisable(false);
            cb_state.setItems(sample.Controller.getTableOfStates(cb_country.getSelectionModel().getSelectedItem()));
            System.out.println("Enabled Combo Box...");
        }
    }

    @FXML private void addRecord() throws SQLException {
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

    @FXML public void initialize() throws SQLException {
        System.out.println("TEST INIT");

        cb_country.setItems(sample.Controller.getTableOfCountrys());
        customerList = helper.Records.getCustomerRecords();

        table.setItems(customerList);
        col_name.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("name"));
        col_ID.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("ID"));
        col_address.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("address"));
        col_postal.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("postalCode"));
        col_division.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("division_ID"));
        col_phone.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("phoneNumber"));
    }

    @FXML public void back() throws Exception {
        Main.switchScene("/sample/homepage.fxml", Main.getStage());
    }

}
