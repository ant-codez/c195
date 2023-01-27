package sample;

import helper.Contact;
import helper.Customer;
import helper.JDBC;
import helper.User;
import helper.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Appointment {
    private static String state = "None";
    private static ObservableList<String> locations = FXCollections.observableArrayList("Phoenix", "Arizona", "White Plains", "New York", "Montreal", "Canada", "London", "England");

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btn_add;

    @FXML
    private Button btn_back;

    @FXML
    private RadioButton rb_month;

    @FXML
    private RadioButton rb_none;

    @FXML
    private RadioButton rb_week;

    @FXML
    private ComboBox<Contact> cb_contact;

    @FXML
    private ComboBox<Customer> cb_customerID;

    @FXML
    private ComboBox<User> cb_userID;

    @FXML private ComboBox<String> cb_location;

    @FXML
    private TableColumn<Appointments, Integer> col_appID;

    @FXML
    private TableColumn<Appointments, Integer> col_contactID;

    @FXML
    private TableColumn<Appointments, Integer> col_customerID;

    @FXML
    private TableColumn<Appointments, Integer> col_description;

    @FXML
    private TableColumn<Appointments, Integer> col_endTime;

    @FXML
    private TableColumn<Appointments, Integer> col_location;

    @FXML
    private TableColumn<Appointments, Integer> col_startTime;

    @FXML
    private TableColumn<Appointments, Integer> col_title;

    @FXML
    private TableColumn<Appointments, Integer> col_type;

    @FXML
    private TableColumn<Appointments, Integer> col_userID;

    @FXML
    private DatePicker dp_date;

    @FXML
    private Label label_title;

    @FXML
    private TableView<Appointments> table;

    @FXML
    private TextField tf_appointmentID;

    @FXML
    private TextField tf_description;

    @FXML
    private TextField tf_endTime;

    @FXML
    private TextField tf_location;

    @FXML
    private TextField tf_startTime;

    @FXML
    private TextField tf_title;

    @FXML
    private TextField tf_type;

    public static void setState(String _state) {
        state = _state;
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        Main.switchScene("/sample/homepage.fxml", Main.getStage());
    }

    @FXML
    void btnClick(ActionEvent event) throws ParseException, SQLException {
        if (state.equals("Add")) {
             if (addAppointment()) {
                 Main.alertSuccess("Successfully added an appointment!!");
                 setUpTable();
             };
        }
        else if (state.equals("Update")) {
            if (updateAppointment()) {
                Main.alertSuccess("Successfully updated Appointment!!");
                setUpTable();
            }
        }
        else if (state.equals("Delete")) {
            if (deleteAppointment()) {
                Main.alertSuccess("Successfully deleted Appointment!!");
                setUpTable();
            }
        }
    }

    boolean deleteAppointment() throws SQLException {
        System.out.println("Deleting Appointment Records");
        Integer ID = Integer.parseInt(tf_appointmentID.getText());

        if (!Main.alertConfirmation("Are you sure you want to delete this Appointment??")) {
            System.out.println("Canceled Deleting Appointment...");
            return false;
        }

        return Appointments.deleteAppointment(ID);
    }

    boolean updateAppointment() throws SQLException, ParseException {
        System.out.println("Updating  appointment...");
        int id = Integer.parseInt(tf_appointmentID.getText());
        Customer customerID = cb_customerID.getSelectionModel().getSelectedItem();
        User user_ID = cb_userID.getSelectionModel().getSelectedItem();
        String title = tf_title.getText();
        String description = tf_description.getText();
        String location = cb_location.getSelectionModel().getSelectedItem();
        Contact contact = cb_contact.getSelectionModel().getSelectedItem();
        String type = tf_type.getText();
        String startTime = tf_startTime.getText();
        String endTime = tf_endTime.getText();
        LocalDate date = dp_date.getValue();
        Timestamp start = Timestamp.valueOf(convertTimeString(startTime).with(date).toLocalDateTime());
        Timestamp end = Timestamp.valueOf(convertTimeString(endTime).with(date).toLocalDateTime());


        Appointments app = new Appointments(customerID.getID(), user_ID.getID(), id, title, description, location, contact.getID(),type, start, end);

        return app.updateAppointment(id);
    }

    boolean addAppointment() throws ParseException, SQLException {
        System.out.println("Adding appointment...");
        Customer customerID = cb_customerID.getSelectionModel().getSelectedItem();
        User user_ID = cb_userID.getSelectionModel().getSelectedItem();
        String title = tf_title.getText();
        String description = tf_description.getText();
        String location = cb_location.getSelectionModel().getSelectedItem();
        Contact contact = cb_contact.getSelectionModel().getSelectedItem();
        String type = tf_type.getText();
        String startTime = tf_startTime.getText();
        String endTime = tf_endTime.getText();
        LocalDate date = dp_date.getValue();
        ZonedDateTime start;
        ZonedDateTime end;

        if (title.isEmpty() || description.isEmpty() || location == null  || type.isEmpty() ||
                startTime.isEmpty() || endTime.isEmpty() || user_ID == null || customerID == null || contact == null || date == null) {
            Main.alertError("Error Adding Appointment, one or more fields have been left blank!!");
            return false;
        }

        if (!validateTime(startTime, endTime)) {
            Main.alertError("Error!! Start time or End time is not in the correct format of hh:mm a. Ex: 08:30 PM");
            return false;
        }

        start = convertTimeString(startTime);
        end = convertTimeString(endTime);

        //check to see if time is within working hours
        if (!isWorkHours(start, end)) {
            return false;
        }

        //if times are overlapping then we return false
        if (isAppOverlapping(Timestamp.valueOf(start.with(date).toLocalDateTime()), Timestamp.valueOf(end.with(date).toLocalDateTime()))) {
            Main.alertError("Error!! Time is Unavailable, please choose another time.");
            return false;
        }

        Appointments app = new Appointments(customerID.getID(), user_ID.getID(), 42, title, description, location, contact.getID(), type, Timestamp.valueOf(start.with(date).toLocalDateTime()), Timestamp.valueOf(end.with(date).toLocalDateTime()));

        return app.addAppointment();
    }

    boolean isWorkHours(ZonedDateTime start, ZonedDateTime end) {
        LocalTime eightAm = LocalTime.of(8, 0);
        LocalTime tenPm = LocalTime.of(22, 0);
        ZonedDateTime estStart = start.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime estEnd = end.withZoneSameInstant(ZoneId.of("America/New_York"));

        if (!estStart.toLocalTime().isAfter(eightAm) || !estStart.toLocalTime().isBefore(tenPm)) {
            Main.alertError("ERROR!! Start Time outside of work hours. Please select a time Between the hours of 8:00 AM and 10:00 PM EST");
            return false;
        }
        else if (!estEnd.toLocalTime().isAfter(eightAm) || !estEnd.toLocalTime().isBefore(tenPm)) {
            Main.alertError("ERROR!! End Time outside of work hours. Please select a time Between the hours of 8:00 AM and 10:00 PM EST");
            return false;
        }

        return true;
    }

    boolean isAppOverlapping(Timestamp start, Timestamp end) throws SQLException {
        System.out.println("Checking if TimeStamps are overlapping");
        ObservableList<Appointments> apps = Appointments.getAppointments();

        for (Appointments appointment: apps) {
           if ((!appointment.getStartTime().after(end) && !appointment.getStartTime().before(start)) || (!appointment.getEndTime().after(start) && !appointment.getEndTime().before(end))) {
                System.out.println("ERROR!! TimeStamps are overlapping!!");
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param startTime String representing the start time of an appointment in hh:mm (AM|PM) format
     * @param endTime String representing the end time of an appointment in hh:mm (AM|PM) format
     * @return true if both parameters are valid time strings else false
     */
    boolean validateTime(String startTime, String endTime) {
        Pattern pattern = Pattern.compile("^([0-1]?[0-9]|2[0-3]):[0-5][0-9] (AM|PM)$");
        Matcher matchStartTime = pattern.matcher(startTime);
        Matcher matchEndTime = pattern.matcher(endTime);

        if (matchEndTime.matches() && matchStartTime.matches())
            return true;

        System.out.println(startTime + "  " + endTime);
        return false;
    }

    ZonedDateTime convertTimeString(String Time) throws ParseException {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mm a");
        LocalTime time = LocalTime.parse(Time, format);
        ZonedDateTime zonedTime = ZonedDateTime.of(LocalDate.now(), time, ZoneId.systemDefault());

        System.out.println("Time = " + Time + " ---> " + zonedTime);
        return zonedTime;
    }

    @FXML
    void getMouseClick(MouseEvent event) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        Appointments selected = table.getSelectionModel().getSelectedItem();

        if (state.equals("Update") || state.equals("Delete")) {
            tf_appointmentID.setText(Integer.toString(selected.getID()));
            tf_description.setText(selected.getDescription());
            tf_endTime.setText(selected.getEndTime().toString());
            tf_startTime.setText(selected.getStartTime().toLocalDateTime().format(formatter));
            tf_endTime.setText(selected.getEndTime().toLocalDateTime().format(formatter));
            tf_title.setText(selected.getTitle());
            tf_type.setText(selected.getType());
            cb_location.setValue(selected.getLocation());
            cb_contact.getSelectionModel().select(selected.getContactID() - 1);
            cb_userID.getSelectionModel().select(selected.getUserID() - 1);
            cb_customerID.getSelectionModel().select(selected.getCustomerID() - 1);
            dp_date.setValue(selected.getStartTime().toLocalDateTime().toLocalDate());
        }
    }

    void setUpTable() throws SQLException {

        table.setItems(Appointments.getAppointments());
        col_appID.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("ID"));
        col_title.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("title"));
        col_description.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("description"));
        col_location.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("location"));
        col_type.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("type"));
        col_startTime.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("startTime"));
        col_endTime.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("endTime"));
        col_contactID.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("contactID"));
        col_userID.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("userID"));
        col_customerID.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("customerID"));
    }

    void setUpComboBoxes() throws SQLException {
        System.out.println("Setting up combo boxes...");

        cb_contact.setItems(JDBC.getAllContacts());
        cb_customerID.setItems(JDBC.getAllCustomers());
        cb_userID.setItems(JDBC.getAllUsers());
        cb_location.setItems(locations);

        cb_contact.setCellFactory(lv -> new ListCell<Contact>() {
            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName());
            }
        });

        cb_customerID.setCellFactory(lv -> new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName() + " - " + item.getID());
            }
        });

        cb_userID.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName() + " - " + item.getID());
            }
        });

        cb_contact.setButtonCell(new ListCell<Contact>() {
            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName());
            }
        });

        cb_customerID.setButtonCell(new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName() + " - " + item.getID());
            }
        });

        cb_userID.setButtonCell(new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName() + " - " + item.getID());
            }
        });

    }

    void checkState() {
        if (state.equals("Add")) {
            label_title.setText("Add Appointment");
            btn_add.setText("Add Appointment");
        } else if (state.equals("Update")) {
            label_title.setText("Update Appointment");
            btn_add.setText("Update Appointment");
        } else if (state.equals("Delete")) {
            label_title.setText("Delete Appointment");
            btn_add.setText("Delete Appointment");
        }

    }

    @FXML
    void radioBtnAction(ActionEvent event) throws SQLException {
        String name = ((RadioButton) event.getSource()).getText();
        System.out.println("Radio button clicked : " + name);


        if (name.equals("Month")) {
            setUpTable();
            Predicate<Appointments> currentMonth = myData ->
                    myData.getStartTime().toLocalDateTime().getMonthValue() == LocalDate.now().getMonthValue();

            table.setItems(table.getItems().filtered(currentMonth));
        } else if (name.equals("Week")) {
            setUpTable();
            Predicate<Appointments> currentWeek = myData ->
                    myData.getStartTime().toLocalDateTime().get(WeekFields.ISO.weekOfWeekBasedYear()) == LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear());

            table.setItems(table.getItems().filtered(currentWeek));
        }
        else if (name.equals("None")) {
            setUpTable();
        }



    }

    @FXML public void initialize() throws ParseException, SQLException {
        System.out.println("INIT");

        setUpComboBoxes();
        setUpTable();
        checkState();
    }

}
