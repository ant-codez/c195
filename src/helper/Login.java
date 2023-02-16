package helper;

import sample.Controller;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login {

    private static String message = "";

    /**
     *
     * @param username - a String representing the username of the user attempting to log in.
     * @param password - a String representing the password of the user attempting to log in.
     * @return If the user is successfully logged in, returns a User object representing the user.
     * If the login attempt fails, returns null.
     */
    public static User login(String username, String password) {
        System.out.println("Attempting to Log in...");
        Connection conn = helper.JDBC.startConnection();
        String sql = String.format("SELECT User_ID, Password FROM client_schedule.users WHERE User_Name = '%s'", username);

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                Integer id = rs.getInt("User_ID");
                String pass = rs.getString("Password");

                System.out.println("Username = " + username + "  Password = " + password + " ID = " + id);

                if (password.equals(pass)) {
                    return new User(id, username, password);
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Error user not found...");

        if (Controller.getUserLanguage().equals("en")) {
            message = "Error! Incorrect username or password";
        }
        else {
            message = "Erreur! identifiant ou mot de passe incorrect";
        }

        return null;
    }

    /**
     * Returns the message set by the most recent call to the login() method.
     * If login() has not been called or if no message has been set, returns null.
     * @return A String representing the message set by the most recent call to login(), or null if no message has been set.
     */
    public static String getMessage() {
        return message;
    }

}
