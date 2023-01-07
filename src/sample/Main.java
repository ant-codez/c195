package sample;
import sample.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Locale;
import helper.JDBC;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Get language of system

        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        JDBC.startConnection();
        launch(args);
    }
}
