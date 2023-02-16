package sample;

import helper.JDBC;
import helper.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage stage;

    /**
     * Starts the application by initializing the primary stage and loading the login screen.
     * @param primaryStage the primary stage for the application.
     * @throws Exception if the login screen FXML file cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    /**
     * Gets the current stage of the application.
     * @return the current stage of the application.
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * Switches the current scene to a new FXML file.
     * @param fileName the name of the FXML file to switch to.
     * @param stage the current stage of the application.
     * @throws IOException if the new FXML file cannot be loaded.
     */
    public static void switchScene(String fileName, Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Controller.class.getResource(fileName));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Displays an error message in a pop-up alert box.
     * @param message the message to display.
     */
    public static void alertError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("ERROR!!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a success message in a pop-up alert box.
     * @param message the message to display.
     */
    public static void alertSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success!!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a confirmation message in a pop-up alert box.
     * @param message the message to display.
     * @return true if the user selects "YES", false if the user selects "NO".
     */
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

    /**
     * The main method that starts the application and initializes the database connection.
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        JDBC.startConnection();
        launch(args);
    }
}
