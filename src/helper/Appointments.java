package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * Holds all appointment data and methods from the database into this class object.
 */
public class Appointments {

    private int customerID;
    private int userID;
    private int ID;
    private int contactID;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp startTime;
    private Timestamp endTime;

    /**
     * Creates a new Appointments object with the specified parameters.
     * @param _customerID the ID of the customer associated with the appointment
     * @param _userID the ID of the user who created the appointment
     * @param _id the ID of the appointment
     * @param _title the title of the appointment
     * @param _description the description of the appointment
     * @param _location the location of the appointment
     * @param _contact the ID of the contact associated with the appointment
     * @param _type the type of the appointment
     * @param _startTime the start time of the appointment
     * @param _endTime the end time of the appointment
     */
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

    /**
     * returns the customer ID of the appointment.
     * @return the customer ID of the appointment.
     */
    public int getCustomerID() { return this.customerID; }

    /**
     * returns the user ID of the appointment.
     * @return the user ID of the appointment.
     */
    public int getUserID() { return this.userID; }

    /**
     * returns the ID of the appointment.
     * @return the ID of the appointment.
     */
    public int getID() { return this.ID; }

    /**
     * returns the title of the appointment
     * @return the title of the appointment
     */
    public String getTitle() { return this.title; }

    /**
     * returns the description of the appointment
     * @return description of the appointment
     */
    public String getDescription() { return this.description; }

    /**
     * returns the location of the appointment
     * @return the location of the appointment
     */
    public String getLocation() { return this.location; }

    /**
     * returns the type of the appointment
     * @return the type of the appointment
     */
    public String getType() { return this.type; }

    /**
     * returns the ID of the contact
     * @return the ID of the contact of the appointment
     */
    public int getContactID() { return this.contactID; }

    /**
     * returns the start time of the appointment
     * @return start time of the appointment
     */
    public Timestamp getStartTime() { return this.startTime; }

    /**
     * returns the end time of the appointment
     * @return end time of the appointment
     */
    public Timestamp getEndTime() { return this.endTime; }

    /**
     * Inserts a new appointment into the client_schedule.appointments table in the database.
     * @return true if the insertion was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */

    public boolean addAppointment() throws SQLException {
        Connection conn = helper.JDBC.startConnection();
        String sql = "INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        System.out.println("Start Time 2 = ");
        System.out.print(this.startTime);

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

        int rowsInserted = ps.executeUpdate();

        if (rowsInserted > 0) {
            System.out.println("Successful Insertion!!");
            return true;
        }

        System.out.println("Insertion unsuccessful!!");
        return false;
    }

    /**
     * Updates an existing appointment in the client_schedule.appointments table in the database.
     * @param ID the ID of the appointment to be updated.
     * @return true if the update was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */

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

    /**
     * Deletes an existing appointment from the client_schedule.appointments table in the database.
     * @param ID the ID of the appointment to be deleted.
     * @return true if the deletion was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */

    public static boolean deleteAppointment(int ID) throws SQLException {
        System.out.println("Deleting Appointment ID: " + ID);
        Connection conn = helper.JDBC.startConnection();
        String sql = "DELETE FROM client_schedule.appointments WHERE Appointment_ID = " + Integer.toString(ID);
        Statement statement = conn.createStatement();

        int rowsDeleted = statement.executeUpdate(sql);
        if (rowsDeleted > 0) {
            System.out.println("Successfully Removed Appointment from Database");
            return true;
        }

        System.out.println("Error Removing Appointment from Database");
        return false;
    }

    /**
     * Retrieves all appointments from the client_schedule.appointments table in the database.
     * @return an ObservableList of all appointments.
     * @throws SQLException if a database access error occurs.
     */

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
