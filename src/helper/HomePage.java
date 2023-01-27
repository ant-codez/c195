package helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import sample.Main;

import java.io.IOException;

public class HomePage {
    private Parent root;

    @FXML
    private Button btn_addRecords;
    @FXML
    private Button btn_deleteRecords;
    @FXML
    private Button btn_updateRecords;

    @FXML public void addRecords() throws IOException {
        sample.Records.setState("Add");
        Main.switchScene("/sample/records.fxml", Main.getStage());
    }

    @FXML public void delete() throws Exception {
        sample.Records.setState("Delete");
        Main.switchScene("/sample/records.fxml", Main.getStage());
    }

    @FXML public void update() throws Exception {
        sample.Records.setState("Update");
        Main.switchScene("/sample/records.fxml", Main.getStage());
    }

    @FXML public void addAppointment() throws Exception {
        sample.Appointment.setState("Add");
        Main.switchScene("/sample/appointment.fxml", Main.getStage());
    }

    @FXML public void deleteAppointment() throws Exception {
        sample.Appointment.setState("Delete");
        Main.switchScene("/sample/appointment.fxml", Main.getStage());
    }

    @FXML public void updateAppointment() throws Exception {
        sample.Appointment.setState("Update");
        Main.switchScene("/sample/appointment.fxml", Main.getStage());
    }
}
