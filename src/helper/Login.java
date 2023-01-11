package helper;

import sample.Controller;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login {

    private static String message = "";
    private static String userID;

    public static boolean login(String username, String password) {
        System.out.println("Attempting to Log in...");
        Connection conn = helper.JDBC.startConnection();
        String sql = String.format("SELECT User_Name, Password FROM client_schedule.users WHERE User_Name = '%s'", username);

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                String User_Name = rs.getString("User_Name");
                String Password = rs.getString("Password");

                System.out.println("Username = " + User_Name + "  Password = " + Password);

                if (Controller.getUserLanguage() == "en") {
                    message = "Log in Success!!";
                }
                else {
                    message = "CHANGE";
                }

                return true;
            }
            else {
                System.out.println("Error user not found...");

                if (Controller.getUserLanguage() == "en") {
                    message = "Error! Incorrect username or password";
                }
                else {
                    message = "Erreur! identifiant ou mot de passe incorrect";
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    public static String getMessage() {
        return message;
    }

}
