package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private int id;
    private int seanceId;
    private int userId;
    private int employeeId;
    private LocalDateTime dateTime;
    private List<Ticket> tickets;

    public Order(int id, int seanceId, int userId, int employeeId, LocalDateTime dateTime, List<Ticket> tickets) {
        this.id = id;
        this.seanceId = seanceId;
        this.userId = userId;
        this.employeeId = employeeId;
        this.dateTime = dateTime;
        this.tickets = tickets;
    }

    public Order(int seanceId, int userId, int employeeId, LocalDateTime dateTime) {
        this.seanceId = seanceId;
        this.userId = userId;
        this.employeeId = employeeId;
        this.dateTime = dateTime;
        this.tickets = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeanceId() {
        return seanceId;
    }

    public void setSeanceId(int seanceId) {
        this.seanceId = seanceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
