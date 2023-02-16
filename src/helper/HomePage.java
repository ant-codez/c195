package helper;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sample.Controller;
import sample.Main;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class HomePage {
    private Parent root;
    @FXML
    private Button btn_addRecords;
    @FXML
    private Button btn_deleteRecords;
    @FXML
    private Button btn_updateRecords;
    @FXML
    private Label label_app;

    /**
     * switches to the records page
     * Sets the state for the Records Class to "Add"
     * @throws IOException If there is an error accessing the fxml file
     */
    @FXML public void addRecords() throws IOException {
        sample.Records.setState("Add");
        Main.switchScene("/sample/records.fxml", Main.getStage());
    }

    /**
     * switches to the records page
     * Sets the state for the Records Class to "Delete"
     * @throws IOException If there is an error accessing the fxml file
     */
    @FXML public void delete() throws Exception {
        sample.Records.setState("Delete");
        Main.switchScene("/sample/records.fxml", Main.getStage());
    }

    /**
     * switches to the records page
     * Sets the state for the Records Class to "Update"
     * @throws IOException If there is an error accessing the fxml file
     */
    @FXML public void update() throws Exception {
        sample.Records.setState("Update");
        Main.switchScene("/sample/records.fxml", Main.getStage());
    }

    /**
     * switches to the appointments page
     * Sets the state for the Appointment Class to "Add"
     * @throws IOException If there is an error accessing the fxml file
     */
    @FXML public void addAppointment() throws Exception {
        sample.Appointment.setState("Add");
        Main.switchScene("/sample/appointment.fxml", Main.getStage());
    }

    /**
     * switches to the appointments page
     * Sets the state for the Appointment Class to "Delete"
     * @throws IOException If there is an error accessing the fxml file
     */
    @FXML public void deleteAppointment() throws Exception {
        sample.Appointment.setState("Delete");
        Main.switchScene("/sample/appointment.fxml", Main.getStage());
    }

    /**
     * switches to the appointments page
     * Sets the state for the Appointment Class to "Update"
     * @throws IOException If there is an error accessing the fxml file
     */
    @FXML public void updateAppointment() throws Exception {
        sample.Appointment.setState("Update");
        Main.switchScene("/sample/appointment.fxml", Main.getStage());
    }

    /**
     * switches to the Reports page
     * Sets the state for the Appointment Class to "Add"
     * @throws IOException If there is an error accessing the fxml file
     */
    @FXML public void viewReports() throws Exception {
        Main.switchScene("/sample/reports.fxml", Main.getStage());
    }

    /**
     * Initialize the Homepage view.
     * @throws ParseException if there is an error parsing the date
     * @throws SQLException if there is an error with the SQL query
     */

    @FXML public void initialize() throws ParseException, SQLException {
        label_app.setText(Controller.homepageMsg);
    }
}
