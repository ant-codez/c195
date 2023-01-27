package helper;

public class User {

        private int ID;
        private String name;
        private String password;

        public User(int _ID, String _name, String _password) {
            this.ID = _ID;
            this.name = _name;
            this.password = _password;
        }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
