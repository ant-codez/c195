package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class Appointments {

    private int customerID;
    private int userID;
    private int ID;
    private int contactID; // might be Customer class later
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp startTime; // Start Time
    private Timestamp endTime;

    public Appointments(int _customerID, int _userID, int _id, String _title, String _description, String _location, int _contact, String _type, Timestamp _startTime, Timestamp _endTime) {
        this.customerID = _customerID;
        this.userID = _userID;
        this.title = _title;
        this.description = _description;
        this.location = _location;
        this.contactID = _contact;
        this.type = _type;
        this.startTime = _startTime;
        this.endTime = _endTime;
        this.ID = _id;
    }

    public int getCustomerID() { return this.customerID; }
    public int getUserID() { return this.userID; }
    public int getID() { return this.ID; }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public String getLocation() { return this.location; }
    public String getType() { return this.type; }
    public int getContactID() { return this.contactID; }
    public Timestamp getStartTime() { return this.startTime; }
    public Timestamp getEndTime() { return this.endTime; }

    public boolean addAppointment() throws SQLException {
        Connection conn = helper.JDBC.startConnection();
        String sql = "INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, this.title);
        ps.setString(2, this.description);
        ps.setString(3, this.location);
        ps.setString(4, this.type);
        ps.setTimestamp(5, Timestamp.valueOf(this.startTime.toLocalDateTime()));
        ps.setTimestamp(6, Timestamp.valueOf(this.endTime.toLocalDateTime()));
        ps.setInt(7, this.customerID);
        ps.setInt(8, this.userID);
        ps.setInt(9, this.contactID);

        int rowsInserted = ps.executeUpdate();

        if (rowsInserted > 0) {
            System.out.println("Successful Insertion!!");
            return true;
        }

        System.out.println("Insertion unsuccessful!!");
        return false;
    }

    public boolean updateAppointment(int ID) throws SQLException {
        Connection conn = helper.JDBC.startConnection();
        String sql = "UPDATE client_schedule.appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, this.title);
        ps.setString(2, this.description);
        ps.setString(3, this.location);
        ps.setString(4, this.type);
        ps.setTimestamp(5, this.startTime);
        ps.setTimestamp(6, this.endTime);
        ps.setInt(7, this.customerID);
        ps.setInt(8, this.userID);
        ps.setInt(9, this.contactID);
        ps.setInt(10, ID);

        int rowsInserted = ps.executeUpdate();

        if (rowsInserted > 0) {
            System.out.println("Successful Update!!");
            return true;
        }

        System.out.println("Error Update failed!!");

        return false;
    }

    public static boolean deleteAppointment(int ID) throws SQLException {
        Connection conn = helper.JDBC.startConnection();
        String sql = "DELETE FROM client_schedule.appointments WHERE Customer_ID = " + Integer.toString(ID);

        Statement statement = conn.createStatement();

        int rowsDeleted = statement.executeUpdate(sql);
        if (rowsDeleted > 0) {
            System.out.println("Successfully Removed Appointment from Database");
            return true;
        }

        System.out.println("Error Removing Appointment from Database");
        return false;
    }

    public static ObservableList<Appointments> getAppointments() throws SQLException {
        Connection conn = helper.JDBC.startConnection();
        String sql = "SELECT * FROM client_schedule.appointments";
        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            int id = rs.getInt("Appointment_ID");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");

            appointmentList.add(new Appointments(customerID, userID, id, title, description, location, contactID, type, start, end));
        }

        return appointmentList;
    }

}
