package helper;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class Login {

    public static boolean login(String username, String password) {

        String sql = String.format("SELECT * FROM client_schedule.users WHERE User_Name = %s AND Password = %s", username, password);
        PreparedStatement ps = JBDC.connection.prepareStatement(sql);

        return true;
    }

}
