package helper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {

    private int ID;
    private String name;
    private String password;

    public User(int _ID, String _name, String _password) {
        this.ID = _ID;
        this.name = _name;
        this.password = _password;
    }
    /**
     * Returns the ID of the User
     * @return ID of the User
     */
    public int getID() {
        return ID;
    }

    /**
     * Returns the name of the User
     * @return name of the User
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the password of the User
     * @return password of the User
     */
    public String getPassword() {
        return password;
    }
    /**
     * Returns the User with the given ID from the database.
     * @param ID the ID of the User to retrieve
     * @return the User with the given ID, or null if the User is not found
     * @throws SQLException if there is an error executing the SQL statement
     */
    public static User getUser(Integer ID) throws SQLException {
        Connection conn = helper.JDBC.startConnection();
        String sql = "SELECT * FROM client_schedule.users";
        User user = null;

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            Integer _ID = rs.getInt("User_ID");
            String name = rs.getString("User_Name");
            String pass = rs.getString("Password");

            user = new User(_ID, name, pass);
        }

        return user;
    }
}
