package helper;

/**
 * Holds all Contact data and methods from the database into this class object.
 */
public class Contact {
    private int ID;
    private String name;
    private String email;

    /**
     * This is a constructor for a Contact class that takes in an ID, name,
     * and email as parameters and initializes the corresponding instance variables with them.
     * @param _ID This is the ID of the contact and is assigned to the instance variable ID.
     * @param _name This is the name of the contact and is assigned to the instance variable
     * @param _email This is the email of the contact and is assigned to the instance variable
     */
    public Contact(int _ID, String _name, String _email) {
        this.ID = _ID;
        this.name = _name;
        this.email = _email;
    }

    /**
     * Returns the ID of the Contact.
     * @return the ID of the Contact
     */
    public int getID() {
        return ID;
    }

    /**
     * Returns the Name of the Contact.
     * @return the Name of the Contact
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the email of the Contact.
     * @return the email of the Contact
     */
    public String getEmail() {
        return email;
    }
}
