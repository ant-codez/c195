package sample;

import helper.Appointments;
import helper.Contact;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Locale;
import java.util.function.Predicate;

public class Reports {
    @FXML
    private TableColumn<Appointments, Integer> col_appID;

    @FXML
    private TableColumn<Appointments, Integer> col_customerID;

    @FXML
    private TableColumn<Appointments, Integer> col_description;

    @FXML
    private TableColumn<Appointments, Integer> col_endTime;

    @FXML
    private TableColumn<Appointments, Integer> col_startTime1;

    @FXML
    private TableColumn<Appointments, Integer> col_title;

    @FXML
    private TableColumn<Appointments, Integer> col_type;

    @FXML
    private TableView<Appointments> table;

    @FXML
    private TextField tf_result;

    @FXML
    private TextField tf_type;

    @FXML private ComboBox<String> cb_month;
    @FXML private ComboBox<Contact> cb_contacts;
    @FXML private ComboBox<String> cb_location;
    private static ObservableList<String> locations = FXCollections.observableArrayList("Phoenix, Arizona", "White Plains, New York", "Montreal, Canada", "London, England");

    /**
     * switches back to the homepage
     * @throws IOException If there is an error accessing the fxml file
     */
    @FXML
    void back(ActionEvent event) throws IOException {
        Main.switchScene("/sample/homepage.fxml", Main.getStage());
    }

    /**
     *Generates a report for the given appointment type and month.
     *Filters appointments by type and month and displays them in a table.
     *Also updates a text field with the total number of appointments found.
     *@throws SQLException If there is an error accessing the database.
     */

    @FXML void report1() throws SQLException {
        String type = tf_type.getText();
        Integer month = cb_month.getSelectionModel().getSelectedIndex() + 1;
        Integer totalFound = 0;

        if (type.isEmpty() || month == null) {
            Main.alertError("Error One or more fields has been left blank!");
            return;
        }

        setUpTable();
        Predicate<Appointments> filteredMonth = myData ->
                myData.getType().toLowerCase(Locale.ROOT).equals(type.toLowerCase(Locale.ROOT)) && myData.getStartTime().toLocalDateTime().getMonthValue() == month;
        table.setItems(table.getItems().filtered(filteredMonth));

        totalFound = table.getItems().size();

        tf_result.setText(Integer.toString(totalFound));
    }

    /**
     * Generates a report for the given contact.
     * Filters appointments by contact and displays them in a table.
     * @throws SQLException If there is an error accessing the database.
     */

    @FXML void report2() throws SQLException {
        Contact contact = cb_contacts.getSelectionModel().getSelectedItem();

        if (contact == null) {
            Main.alertError("Error. Please select one contact to view schedule...");
            return;
        }

        setUpTable();
        Predicate<Appointments> filteredContacts = myData ->
                myData.getContactID() == contact.getID();
        table.setItems(table.getItems().filtered(filteredContacts));
    }

    /**
     * Generates a report for the given location.
     * Filters appointments by location and displays them in a table.
     * @throws SQLException If there is an error accessing the database.
     */
    @FXML void report3() throws SQLException {
        String location = cb_location.getSelectionModel().getSelectedItem();

        if (location == null) {
            Main.alertError("Error. Please select one country to view Report...");
            return;
        }

        setUpTable();
        Predicate<Appointments> filteredContacts = myData ->
                myData.getLocation().equals(location);
        table.setItems(table.getItems().filtered(filteredContacts));
    }

    /**
     * Sets up the table by retrieving appointments from the database and assigning them to the table.
     * Also sets up the table columns and cell values.
     * @throws SQLException If there is an error accessing the database.
     */
    private void setUpTable() throws SQLException {
        table.setItems(Appointments.getAppointments());
        col_appID.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("ID"));
        col_title.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("title"));
        col_description.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("description"));
        col_type.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("type"));
        col_startTime1.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("startTime"));
        col_endTime.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("endTime"));
        col_customerID.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("customerID"));
    }

    /**
     * Set up combo boxes for the report view.
     * @throws SQLException if there is an error with the SQL query
     */
    private void setUpComboBoxes() throws SQLException {
        System.out.println("Setting up combo boxes...");

        String[] months = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};

        cb_month.getItems().addAll(months);
        cb_contacts.setItems(JDBC.getAllContacts());
        cb_location.setItems(locations);

        cb_contacts.setCellFactory(lv -> new ListCell<Contact>() {
            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName());
            }
        });

        cb_contacts.setButtonCell(new ListCell<Contact>() {
            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName());
            }
        });
    }
    /**
     * Initialize the report view.
     * @throws ParseException if there is an error parsing the date
     * @throws SQLException if there is an error with the SQL query
     */
    @FXML public void initialize() throws ParseException, SQLException {
        System.out.println("INIT REPORTS");

        setUpTable();
        setUpComboBoxes();
    }



}

