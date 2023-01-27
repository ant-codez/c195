package helper;

public class Contact {
    private int ID;
    private String name;
    private String email;

    public Contact(int _ID, String _name, String _email) {
        this.ID = _ID;
        this.name = _name;
        this.email = _email;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
