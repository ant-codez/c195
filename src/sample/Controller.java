package sample;

import helper.Appointments;
import helper.JDBC;
import helper.Login;
import helper.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;

public class Controller {

    @FXML
    private Button btn_submit;
    @FXML
    private Label label_banner;
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
    public static User currentUser;
    public static String homepageMsg = "No upcoming appointments.";

    public static String getUserLanguage() {
        return userLanguage;
    }
    public static void setCurrentUser(Integer id) throws SQLException { currentUser = User.getUser(id); }

    /**
     * Determines the language to use based on the user's language preference.
     * If the language is French, it translates certain labels to French.
     */
    public void determinLang() {
        System.out.println(userLanguage);

        if (userLanguage.equals("fr")) {
            //translate to french
            label_zoneID.setText("ID de zone : France");
            label_password.setText("Mot de passe");
            label_username.setText("Nom d'utilisateur");
            btn_submit.setText("Identifiant");
            label_banner.setText("Identifiant");
        }
        else {
            label_zoneID.setText("Zone ID : " + ZoneId.systemDefault());
        }
    }

    /**
     * Authenticates the user by checking if the username and password match with a user in the system.
     * If the login is successful, it switches the scene to the home page.
     * If the login is unsuccessful, it displays an error message.
     * @throws IOException if the new FXML file cannot be loaded.
     */
    public void login() throws IOException {
        String username = tf_username.getText();
        String password = tf_password.getText();

        System.out.println(username + "  " + password);
        User logedinUser = helper.Login.login(username, password);

        if (logedinUser != null) {
            try {
                checkUserAppointment(logedinUser);
                writeLoginAttempt(true, username);
                Main.switchScene("/sample/homePage.fxml", Main.getStage());
                return;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            writeLoginAttempt(false, username);
        }

        Main.alertError(Login.getMessage());
    }

    /**
     * Writes to a text file the login attempt details, such as whether the login was successful and the username used.
     * @param success Boolean indicating whether the login was successful or not.
     * @param username The username used in the login attempt.
     * @throws IOException if the new FXML file cannot be loaded.
     */

    void writeLoginAttempt(Boolean success, String username) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dateTime = dateFormat.format(date);
        FileWriter fileWriter = new FileWriter("login_activity.txt", true);
        BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
        String writeString;

        if (success) {
            writeString = "User " + username + " successfully logged in at " + dateTime;
        }
        else {
            writeString = "User " + username + " gave invalid log in at " + dateTime;
        }

        bufferWriter.write(writeString);
        bufferWriter.newLine();
        bufferWriter.close();
        fileWriter.close();
    }

    /**
     * Retrieves a table of states for a specified country from the database.
     * @param country The name of the country to retrieve states for.
     * @return An ObservableList of the names of the states of the specified country.
     */

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

    /**
     * Retrieves a table of countries from the database.
     * @return An ObservableList of the names of the countries in the database.
     */

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

    /**
     * Checks if the specified user has an appointment within the next 15 minutes.
     * If so, displays a success message and sets a homepage message with details of the appointment.
     * @param user The user to check for upcoming appointments.
     * @throws SQLException if error accessing database
     */

    public void checkUserAppointment(User user) throws SQLException {
        System.out.println("Checking if user has an appointment in the next 15 minutes...");
        ObservableList<Appointments> appList = Appointments.getAppointments();
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault());

        for (Appointments app: appList) {
            ZonedDateTime appStartTime = app.getStartTime().toLocalDateTime().atZone(ZoneId.of("UTC"));

            if (app.getUserID() == user.getID()) {
                Duration duration = Duration.between(now, appStartTime);

                if (duration.getSeconds() <= 900 && duration.getSeconds() >= 0) {
                    System.out.println("Appointment within 15 minutes!!!!");
                    Main.alertSuccess(user.getName() + " Has an appointment in the next 15 minutes!!");
                    homepageMsg = String.format("Appointment ID %s on %s. In less than 15 minutes", app.getID(), app.getStartTime());
                }
            }
        }
    }

    /**
     * Initializes the controller by determining the language to use.
     */
    @FXML public void initialize() {
        System.out.println("INIT");

        determinLang();
    }

}
