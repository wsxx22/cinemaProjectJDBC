import model.*;
import model.account.Employee;
import model.account.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Cinema {

    private Database database;

    public Cinema() {
        database = new Database();
    }

    public User loginUser(String username, String password) {
        return database.loginUser(username, password);
    }

    public User userRegister(String username, String password, String email) {
        return database.userRegister(username, password, email);
    }

    public Employee loginEmployee(String username, String password) {
        return database.logInEmployee(username, password);
    }

    public boolean addMovie(Movie movie) {
        return database.addMovie(movie);
    }

    public List<Movie> getMovies() {
        return database.getMovies();
    }

    public Room addRoom (String name, int seats, int rows) {
        return database.addRoom(name, seats, rows);
    }

    public List<Room> getRooms() {
        return database.getRooms();
    }

    public boolean addSeance (Seance seance) {
        return database.addSeance(seance);
    }

    public TicketType addTicketType (String type, int price) {
        return database.addTicketType(type, price);
    }

    public List<TicketType> showTicketTypes () {
        return database.showTicketTypes();
    }

    public boolean isSeanceColliding (Seance seance) {
        return database.isSeanceColliding(seance);
    }

    public Map<Seance, Movie> getSeanceToOrder (LocalDate seanceDateSelectedByUser) {
        return database.getSeanceToOrder(seanceDateSelectedByUser);
    }

    public boolean isFreeSeat (int seanceId, int row, int seat, String dateTimeSeance) {
        return database.isFreeSeat(seanceId, row, seat, dateTimeSeance);
    }

    public Ticket addTicket (int idSeance, String ticketType, int row, int seat) {
        return database.addTicket(idSeance, ticketType, row, seat);
    }

    public List<Seat> getSeatsTakenForSeance(Seance seance) {
        return database.getSeatsTakenForSeance(seance);
    }

    public void addOrder(Order order) {
        database.addOrder(order);
    }

    public void showBoughtUserTickets (String username) {
        database.showBoughtUserTickets(username);
    }
}
