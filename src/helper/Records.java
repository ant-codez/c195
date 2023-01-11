package helper;

import helper.JDBC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Records {

    public static Boolean addCustomer(String name, String address, String number, String postal, String division_id) throws SQLException {
        Connection conn = helper.JDBC.startConnection();
        String sql = String.format("INSERT INTO client_schedule.customers (Customer_Name, Address, Postal_Code, Phone, Division_ID), (%s,%s,%s,%s,%s)", name, address, postal, number, division_id);

        try {
            Statement statement = conn.createStatement();
            int rowsInserted = statement.executeUpdate(sql);

            if (rowsInserted > 0) {
                System.out.println("Successful Insertion!!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }

        return false;
    }
}
