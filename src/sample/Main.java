package sample;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import sample.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.Locale;
import helper.JDBC;

public class Main extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public static void switchScene(String fileName, Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Controller.class.getResource(fileName));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void alertError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("ERROR!!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void alertSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success!!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Boolean alertConfirmation(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Are you Sure?");
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType yes = new ButtonType("YES");
        ButtonType no = new ButtonType("NO");
        alert.getButtonTypes().setAll(yes, no);

        var result = alert.showAndWait();

        if (result.get() == yes) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void main(String[] args) {
        JDBC.startConnection();
        launch(args);
    }
}
