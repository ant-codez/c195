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

    public String getState_Name() {
        return state_Name;
    }

    public String getCountry_Name() {
        return country_Name;
    }

    public int getCountry_id() {
        return country_id;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public int getID() {
        return this.ID;
    }

    public int getDivision_ID() {
        return this.division_ID;
    }
}
