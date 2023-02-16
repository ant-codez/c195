package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class Records {

    /**
     * Inserts a new customer record into the database.
     * @param name the name of the customer
     * @param address the address of the customer
     * @param number the phone number of the customer
     * @param postal the postal code of the customer
     * @param division_id the ID of the division to which the customer belongs
     * @return true if the customer record was successfully inserted, false otherwise
     * @throws SQLException if there was an error executing the SQL statement
     */
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

    /**
     * Retrieves all customer records from the database.
     * @return an ObservableList of Customer objects representing all customer records in the database
     * @throws SQLException if there was an error executing the SQL statement
     */
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

    /**
     * Updates an existing customer record in the database.
     * @param name the updated name of the customer
     * @param address the updated address of the customer
     * @param postal the updated postal code of the customer
     * @param phone the updated phone number of the customer
     * @param division_ID the updated ID of the division to which the customer belongs
     * @param ID the ID of the customer record to update
     * @return true if the customer record was successfully updated, false otherwise
     * @throws SQLException if there was an error executing the SQL statement
     */
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
    /**
     * Deletes a customer record from the database.
     * @param userID the ID of the customer record to delete
     * @return true if the customer record was successfully deleted, false otherwise
     * @throws Exception if there was an error executing the SQL statement
     */
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
    /**
     * Deletes all appointments associated with a customer from the database.
     * @param userID the ID of the customer whose appointments should be deleted
     * @throws Exception if there was an error executing the SQL statement
     */
    public static void deleteAppointments(Integer userID) throws Exception {
        System.out.println("Removing all associated appoints to customer...");
        ObservableList<Appointments> appList = Appointments.getAppointments();

        for (Appointments app : appList) {
            if (app.getCustomerID() == userID) {
                System.out.println("Found Record with Customer ID = " + userID);
                Appointments.deleteAppointment(app.getID());
            }
        }

    }
}
