package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.sql.*;
import java.util.HashMap;

public abstract class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ip = "//localhost/";
    private static final String dbName = "client_schedule";

    private static final String url = protocol + vendorName + ip + dbName + "?connectionTimeZone=SERVER";

    private static final String MYSQLJBDriver = "com.mysql.cj.jdbc.Driver";

    private static final String username = "sqlUser";
    private static final String password = "Passw0rd!";



    private static Connection conn = null;

    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJBDriver);
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connect Successfully!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed!!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static HashMap<Integer, String> getAllStates() throws SQLException{
        //get table of states
        Connection conn = startConnection();
        String sql = "SELECT * FROM client_schedule.first_level_divisions;";
        HashMap<Integer, String> statesHashMap = new HashMap<Integer, String>();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            String id = rs.getString("Division_ID");
            statesHashMap.put(Integer.parseInt(id), rs.getString("Division"));
        }

        return statesHashMap;
    }



}
