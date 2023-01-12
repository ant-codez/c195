package sample;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import helper.Login;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {
    private Parent root;

    @FXML
    private Button btn_submit;
    @FXML
    private Label label_banner;
    @FXML
    private Label label_message;
    @FXML
    private Label label_password;
    @FXML
    private Label label_username;
    @FXML
    public Label label_zoneID;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_username;

    private static String userLanguage = System.getProperty("user.language");
    public static HashMap<String, Integer> statesHashMap = new HashMap<String, Integer>();

    public static String getUserLanguage() {
        return userLanguage;
    }

    public void determinLang() {
        System.out.println(userLanguage);

        if (userLanguage == "fr") {
            //translate to french
            label_zoneID.setText("ID de zone : France");
            label_password.setText("Mot de passe");
            label_username.setText("Nom d'utilisateur");
            btn_submit.setText("Identifiant");
            label_banner.setText("Identifiant");
        }
        else {
            label_zoneID.setText("Zone ID : America");
        }
    }

    public void login() {
        String username = tf_username.getText();
        String password = tf_password.getText();

        System.out.println(username + "  " + password);
        Boolean login = helper.Login.login(username, password);

        if (login) {
            try {
                Main.switchScene("/sample/homePage.fxml", Main.getStage());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        label_message.setText(Login.getMessage());
    }

    public static ObservableList<String> getTableOfStates(String country) {
        //get table of states
        Connection conn = JDBC.startConnection();
        String sql = "SELECT * FROM client_schedule.first_level_divisions;";
        ObservableList<String> states = FXCollections.observableArrayList();

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String id = rs.getString("Division_ID");

                if (country.equals("UK") && Integer.parseInt(id) >= 101) {
                    states.add(rs.getString("Division"));
                }
                else if (country.equals("U.S") && Integer.parseInt(id) <= 54) {
                    states.add(rs.getString("Division"));
                }
                else if (country.equals("Canada") && (Integer.parseInt(id) >= 60 && Integer.parseInt(id) <= 72)){
                    states.add(rs.getString("Division"));
                }

                statesHashMap.put(rs.getString("Division"), Integer.parseInt(id));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return states;
    }

    public static ObservableList<String> getTableOfCountrys() {
        //get table of states
        Connection conn = JDBC.startConnection();
        String sql = "SELECT * FROM client_schedule.countries;";
        ObservableList<String> country = FXCollections.observableArrayList();

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                country.add(rs.getString("Country"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return country;
    }

    @FXML public void initialize() {
        System.out.println("INIT");

        determinLang();
    }

}
