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
        Main.switchScene("/sample/records.fxml", Main.getStage());
    }
}
