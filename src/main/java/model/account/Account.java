package model.account;

public class Account {

    protected int id;
    protected String username;
    protected String password;
    protected String email;

    public Account(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String toString() {
        return "username: " + getUsername() +" haslo: " + getPassword();
    }
}
