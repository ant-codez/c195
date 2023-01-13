package helper;

import helper.JDBC;
import helper.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Vector;

public class Records {

    public static Boolean addCustomer(String name, String address, String number, String postal, Integer division_id) throws SQLException {
        Connection conn = helper.JDBC.startConnection();
        String sql = "INSERT INTO client_schedule.customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postal);
            ps.setString(4, number);
            ps.setInt(5, division_id);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Successful Insertion!!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static ObservableList<Customer> getCustomerRecords() throws SQLException{
        Connection conn = helper.JDBC.startConnection();
        String sql = "SELECT * FROM client_schedule.customers";
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String number = rs.getString("Phone");
            Integer ID = rs.getInt("Customer_ID");
            Integer division = rs.getInt("Division_ID");

            Customer customer = new helper.Customer(ID, name, address, postalCode, number, division);
            customerList.add(customer);
        }

        return customerList;
    }
    //(Customer_Name, Address, Postal_Code, Phone, Division_ID)

    public static Boolean updateCustomerRecord(String name, String address, String postal, String phone, int division_ID, int ID) throws SQLException {
        Connection conn = helper.JDBC.startConnection();
        String sql = "UPDATE client_schedule.customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postal);
        ps.setString(4, phone);
        ps.setInt(5, division_ID);
        ps.setInt(6, ID);

        int rowsInserted = ps.executeUpdate();

        if (rowsInserted > 0) {
            System.out.println("Successful Update!!");
            return true;
        }

        System.out.println("Error Update failed!!");

        return false;
    }

    public static Boolean deleteCustomerRecord(Integer userID) throws Exception {
        Connection conn = helper.JDBC.startConnection();
        String sql = "DELETE FROM client_schedule.customers WHERE Customer_ID = " + Integer.toString(userID);

        Statement statement = conn.createStatement();

        int rowsDeleted = statement.executeUpdate(sql);
        if (rowsDeleted > 0) {
            System.out.println("Successfully Removed User from Database");
            return true;
        }

        System.out.println("Error Removing User from Database");
        return true;
    }
}
