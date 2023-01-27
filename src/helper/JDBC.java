package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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

    public static ObservableList<User> getAllUsers() throws SQLException {
        Connection conn = startConnection();
        String sql = "SELECT * FROM client_schedule.users";
        ObservableList<User> userList = FXCollections.observableArrayList();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            int id = rs.getInt("User_ID");
            String name = rs.getString("User_Name");
            String password = rs.getString("Password");

            userList.add(new User(id, name, password));
        }

        return userList;
    }

    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        Connection conn = startConnection();
        String sql = "SELECT * FROM client_schedule.customers;";
        ObservableList<Customer> userList = FXCollections.observableArrayList();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            int id = rs.getInt("Customer_ID");
            int division_id = rs.getInt("Division_ID");
            String name = rs.getString("Customer_name");
            String address = rs.getString("Address");
            String postal = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");

            userList.add(new Customer(id, name, address, postal, phone, division_id));
        }

        return userList;
    }

    public static ObservableList<Contact> getAllContacts() throws SQLException {
        Connection conn = startConnection();
        String sql = "SELECT * FROM client_schedule.contacts;";
        ObservableList<Contact> userList = FXCollections.observableArrayList();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            int id = rs.getInt("Contact_ID");
            String name = rs.getString("Contact_Name");
            String email = rs.getString("Email");

            userList.add(new Contact(id, name, email));
        }

        return userList;
    }



}
