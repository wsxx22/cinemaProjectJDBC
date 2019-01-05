package model.account;

public class User extends Account {

    public User(int id, String username, String password, String email) {
        super(id, username, password, email);
    }

    public User(String username, String password, String email) {
        super(username, password, email);
    }



}
