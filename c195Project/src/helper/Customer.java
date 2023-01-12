package helper;

public class Customer {
    private int     ID;
    private String  name;
    private String  address;
    private String  postalCode;
    private String  phoneNumber;
    private int     division_ID;

    public Customer(int _ID, String _name, String _address, String _postalCode, String _phoneNumber, int _division_ID) {
        this.ID = _ID;
        this.name = _name;
        this.address = _address;
        this.postalCode = _postalCode;
        this.phoneNumber = _phoneNumber;
        this.division_ID = _division_ID;
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
