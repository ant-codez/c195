package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;


public class Records {
    @FXML
    private Button btn_add;
    @FXML
    private ComboBox<String> cb_country;
    @FXML
    private ComboBox<String> cb_state;
    @FXML
    private TextField tf_address;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_phoneNumber;
    @FXML
    private TextField tf_postalCode;

    @FXML public void initialize() {
        System.out.println("TEST INIT");

        cb_country.setItems(sample.Controller.getTableOfCountrys());
    }

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

        helper.Records.addCustomer(Name, address, phoneNumber, postalCode, state);
    }

}
