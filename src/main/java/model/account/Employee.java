package model.account;

public class Employee extends Account {

    public Employee(int id, String username, String password, String email) {
        super(id, username, password, email);
    }

    public Employee(String username, String password, String email) {
        super(username, password, email);
    }
}
