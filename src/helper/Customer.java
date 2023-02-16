package helper;

import java.sql.SQLException;
import java.util.HashMap;

public class Customer {
    private int     ID;
    private String  name;
    private String  address;
    private String  postalCode;
    private String  phoneNumber;
    private int     division_ID;
    private int     country_id;
    private String  state_Name;
    private String  country_Name;

    public Customer(int _ID, String _name, String _address, String _postalCode, String _phoneNumber, int _division_ID) throws SQLException {
        HashMap<Integer, String> statesHashMap = helper.JDBC.getAllStates();

        this.ID = _ID;
        this.name = _name;
        this.address = _address;
        this.postalCode = _postalCode;
        this.phoneNumber = _phoneNumber;
        this.division_ID = _division_ID;

        if (division_ID <= 54) {
            this.country_id = 1;
            this.country_Name = "U.S";
        } else if (division_ID >= 60 && division_ID <= 72) {
            this.country_id = 3;
            this.country_Name = "Canada";
        }
        else if (division_ID >= 101 && division_ID <= 104) {
            this.country_id = 2;
            this.country_Name = "UK";
        }

        this.state_Name = statesHashMap.get(this.division_ID);
    }

    /**
     * Retrieves the name of the state associated with the address.
     * @return The name of the state.
     */
    public String getState_Name() {
        return state_Name;
    }

    /**
     * Retrieves the name of the country associated with the address.
     * @return The name of the country.
     */
    public String getCountry_Name() {
        return country_Name;
    }

    /**
     * Retrieves the id of the country associated with the address.
     * @return The id of the country.
     */
    public int getCountry_id() {
        return country_id;
    }

    /**
     * Retrieves the name associated with the address.
     * @return The name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the address.
     * @return The address.
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Retrieves the postal code associated with the address.
     * @return The postal code.
     */
    public String getPostalCode() {
        return this.postalCode;
    }

    /**
     * Retrieves the phone number associated with the address.
     * @return The phone number.
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * Retrieves the ID of the address.
     * @return The ID.
     */
    public int getID() {
        return this.ID;
    }

    /**
     * Retrieves the ID of the division associated with the address.
     * @return The ID of the division.
     */
    public int getDivision_ID() {
        return this.division_ID;
    }
}
