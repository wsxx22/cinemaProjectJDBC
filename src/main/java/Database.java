import model.*;
import model.account.Employee;
import model.account.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://77.55.192.67:3306/wsx22";
    private static String login = "wsx22";
    private static String password = "wsx22";


    private Connection conn;
    private PreparedStatement ps;
    private String sql;
    private ResultSet rs;
    static List<User> usersList = new ArrayList<>();

    public Database() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, login, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close ()  {

            try {
                if (conn != null) conn.setAutoCommit(true);
                if (ps != null) ps.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void showMovies () {

        sql = "SELECT * FROM movies";

        try {

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {

                System.out.println(rs.getInt("id_movie") + rs.getString("title")
                        + rs.getString("description") + rs.getInt("duration") );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public boolean addMovie(Movie movie) {

        sql = "INSERT INTO movies(title, description, duration) VALUES (?,?,?)";

        try {
            conn.setAutoCommit(false); // jesli nie napisze conn.commit to sie insert nie robi
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // ustawia zeby uzyc rs.getInt
            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getDescription());
            ps.setInt(3, movie.getDuration());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                int movieId = rs.getInt(1);

                addMovieDirectors(movieId, movie.getDirectors());
                addMovieActors(movieId, movie.getActors());
                addMovieGenres(movieId, movie.getGenres());

                conn.commit();
                return true;

            } else {
                conn.rollback(); // cofam dodane zmiany w bazie, tutaj insert // blad bazy itp
            }

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            close();
        }

        return false;
    }

    private void addMovieGenres(int movieId, List<String> genres) throws SQLException {

        int genreId;

        for (String g : genres) {
            if (isGenreInDatabase(g)) {
                genreId = getGenreId(g);
            } else {
                genreId = addGenre(g);
            }

            addGenreToMovie(movieId, genreId);
        }
    }

    private void addGenreToMovie (int movieId, int genreId) throws SQLException {

        sql = "INSERT INTO movie_genres(id_movie, id_genre) VALUES(?,?)";

        ps = conn.prepareStatement(sql);
        ps.setInt(1, movieId);
        ps.setInt(2, genreId);
        ps.executeUpdate();

    }

    private int getGenreId(String g) throws SQLException {
        sql = "SELECT id_genre FROM genres WHERE type_genre=?";

        ps = conn.prepareStatement(sql);
        ps.setString(1, g);
        rs = ps.executeQuery();

        return rs.next() ? rs.getInt("id_genre") : 0;
    }

    private int addGenre(String g) throws SQLException {

        sql = "INSERT INTO genres(type_genre) VALUES(?)";

        ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, g);
        ps.executeUpdate();

        rs = ps.getGeneratedKeys();

        return rs.next() ? rs.getInt(1) : 0;

    }

    private boolean isGenreInDatabase(String genre) throws SQLException {
        sql = "SELECT * FROM genres WHERE type_genre=?"; // jako while

        ps = conn.prepareStatement(sql);
        ps.setString(1, genre);
        rs = ps.executeQuery();

        return rs.next();

    }


    private void addMovieActors(int movieId, List<Actor> actors) throws SQLException {
        int actorId;

        for (Actor a : actors) {
            if (isActorInDatabase(a)) {
                actorId = getActor(a);
            } else {
                actorId = addActor(a);
            }
            addActorToMovie(actorId, movieId);
        }
    }

    private void addActorToMovie(int actorId, int movieId) throws SQLException {

        sql = "INSERT INTO movie_actors(id_movie, id_actor) VALUES(?,?)";

        ps = conn.prepareStatement(sql);
        ps.setInt(1, movieId);
        ps.setInt(2, actorId);
        ps.executeUpdate();

    }

    private int addActor(Actor a) throws SQLException {
            sql = "INSERT INTO actors(name, surname) VALUES (?,?)";

            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, a.getName());
            ps.setString(2, a.getSurname());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
    }

    private int getActor(Actor a) throws SQLException {

        sql = "SELECT id_actor FROM actors WHERE name=? AND surname=?";

        ps = conn.prepareStatement(sql);
        ps.setString(1, a.getName());
        ps.setString(2, a.getSurname());
        rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1); // zwracamy id actora jak go znalezlismy
        } else {
            return 0;
        }

    }

    private boolean isActorInDatabase (Actor actor) throws SQLException {
        sql = "SELECT * FROM actors WHERE name=? AND surname=?";

        ps = conn.prepareStatement(sql);
        ps.setString(1, actor.getName());
        ps.setString(2, actor.getSurname());
        rs = ps.executeQuery();

        return rs.next();
    }

    private void addMovieDirectors(int movieId, List<Director> directors) throws SQLException {

        int directorId;

        for (Director d : directors){
            if (isDirectorInDatabes(d)) {
                directorId = getDirectorId(d);
            } else {
                directorId = addDirector(d);
            }

            addDirectorToMovie (movieId, directorId );
        }

    }

    private int getDirectorId(Director d) throws SQLException {

        sql = "SELECT id_director FROM directors WHERE name=? AND surname=?";

        ps = conn.prepareStatement(sql);
        ps.setString(1, d.getName());
        ps.setString(2, d.getSurname());

        rs = ps.executeQuery();

        return rs.next() ? rs.getInt(1) : 0;

//        if (rs.next()) {
//            return rs.getInt("id_director");
//        } else {
//            return 0;
//        }
    }

    private void addDirectorToMovie (int movieId, int directorId) throws SQLException {

        sql = "INSERT INTO movie_directors(id_movie, id_director) VALUES (?,?)";

        ps = conn.prepareStatement(sql);
        ps.setInt(1, movieId);
        ps.setInt(2, directorId);
        ps.executeUpdate();

    }

    private int addDirector(Director d) throws SQLException {
        sql = "INSERT INTO directors (id_director, name, surname) VALUES (NULL, ?, ?)";

        ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, d.getName());
        ps.setString(2, d.getSurname());
        ps.executeUpdate();

        rs = ps.getGeneratedKeys();
//        if (rs.next()) {
//            return rs.getInt("id_director");
//        } else {
//            return 0;
//        }

        return rs.next() ? rs.getInt(1) : 0;

    }

    private boolean isDirectorInDatabes (Director director) throws SQLException {

        sql = "SELECT * FROM directors WHERE name=? AND surname=?";

        ps = conn.prepareStatement(sql);
        ps.setString(1, director.getName());
        ps.setString(2, director.getSurname());
        rs = ps.executeQuery();

        return rs.next(); // jesli zwroci true to istnieje


    }

    public boolean addSeance (Seance seance) {
        sql = "INSERT INTO seances(id_movie, id_room, datetime) VALUES(?,?,?)";

        try {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, seance.getMovieId());
            ps.setInt(2, seance.getRoomId());
            ps.setString(3, seance.getLocalDateTime().toString());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();

            return rs.next();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public User userRegister (String username, String password, String email) { //int
        sql = "INSERT INTO `users` (`id_user`, `username`, `password`, `email`) VALUES (NULL, ?, ?, ?)";

        User user = null;

        try {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                user = new User(
                        rs.getInt(1),
                        username,
                        password,
                        email
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

//    private boolean isUserListContain (User user) {
//        if (users2.contains(user)) {
//            for (int i=1; i<100; i++) {
//                User "user+ i" = new User();
//            }
//        }
//    }

    public User loginUser (String username, String password) {
        sql = "SELECT * FROM users WHERE username=? AND password=?";

        User user = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                user =  new User(
                        rs.getInt("id_user"),
                        username,
                        password,
                        rs.getString("email")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public Employee logInEmployee(String username, String password) {
        sql = "SELECT * from employee WHERE username=? AND password=?";

        Employee employee = null;

            try {
                ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                rs = ps.executeQuery();

                if (rs.next()) {
                    employee = new Employee(
                            rs.getInt("id_employee"),
                            username,
                            password,
                            rs.getString("email")
                    );
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return employee;
    }


    public Room addRoom (String name, int seats, int rows) {
    sql = "INSERT INTO rooms(name, seats) VALUE (?,?)";
    Room room = null;

        try {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setInt(2, seats);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();

            if (rs.next()){
                room = new Room(rs.getInt(1), name, seats, rows);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }

    public List<Room> getRooms() {
        sql = "SELECT * FROM rooms";

        List<Room> rooms = new ArrayList<>();

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getInt(4))
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    public TicketType addTicketType (String type, int price) {

        sql = "INSERT INTO ticket_types(ticket_type, price) VALUES (?,?)";
        TicketType ticketType = null;

        try {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, type);
            ps.setInt(2, price);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                ticketType = new TicketType(type, price);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ticketType;
    }

    public List<TicketType> showTicketTypes () {

            sql = "SELECT * FROM ticket_types";
            List<TicketType> ticketTypes = new ArrayList<>();
            try {
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();

                while (rs.next()) {
                    ticketTypes.add(new TicketType(rs.getString(1), rs.getInt(2)));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return ticketTypes;
    }

    public boolean isSeanceColliding (Seance seance) {

        sql = "SELECT * FROM seances, movies WHERE id_room = ? AND (? >= DATE_ADD(datetime, INTERVAL duration MINUTE) " +
                "OR (? < datetime AND DATE_ADD(?, INTERVAL (SELECT duration FROM movies WHERE id_movie = ?) minute) ) " +
                "<= datetime)";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, seance.getRoomId());
            ps.setString(2, seance.getLocalDateTime().toString());
            ps.setString(3, seance.getLocalDateTime().toString());
            ps.setString(4, seance.getLocalDateTime().toString());
            ps.setInt(5,seance.getMovieId());
            rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Map<Seance, Movie> getSeanceToOrder (LocalDate seanceDateSelectedByUser) {
        sql = "SELECT * FROM seances, movies, rooms WHERE seances.id_movie = movies.id_movie " +
                "AND seances.id_room = rooms.id_room AND CAST(datetime as DATE) = ?";

        Map<Seance, Movie> map = new HashMap<>();

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, seanceDateSelectedByUser.toString());
            rs = ps.executeQuery();

            Seance seance;
            Movie movie;
            while (rs.next()) {
                seance = new Seance(
                        rs.getInt("id_seance"),
                        rs.getInt("id_movie"),
                        rs.getInt("id_room"),
                        LocalDateTime.parse(rs.getString("datetime"),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                );

                movie = new Movie(
                        rs.getInt("id_movie"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("duration")
                );

                map.put(seance, movie);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    public boolean isFreeSeat (int seanceId, int row, int seat, String dateTimeSeance) {

        sql = "SELECT * FROM seances, tickets WHERE tickets.id_seance=? AND row=? AND seat=? AND datetime=? " +
                "AND seances.id_seance = tickets.id_seance";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1,seanceId);
            ps.setInt(2,row);
            ps.setInt(3, seat);
            ps.setString(4, dateTimeSeance);
            rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Ticket addTicket (int idSeance, String ticketType, int row, int seat) {

        sql = "INSERT INTO tickets(id_seance, ticket_type, row, seat) VALUES (?,?,?,?)";

        Ticket ticket = null;
        try {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, idSeance);
            ps.setString(2, ticketType);
            ps.setInt(3,row);
            ps.setInt(4,seat);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                ticket = new Ticket(rs.getInt(1), idSeance, ticketType, row, seat);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ticket;
    }

    public void showBoughtUserTickets (String username) {
        sql = "SELECT movies.title, seances.datetime, orders.datetime, users.username FROM orders, seances, movies, users " +
                "WHERE orders.id_seance = seances.id_seance AND seances.id_movie = movies.id_movie AND " +
                "orders.id_user = users.id_user AND users.username = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                        "Tytuł: " + rs.getString(1)
                        + ", Czas seansu: " + rs.getString(2)
                        + ", Czas zamówienia: " + rs.getString(3)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



















    // -----------------------------------------------------------------------------------------------------------------


    public void addActor (String name, String surname) {
        try {
            ps = conn.prepareStatement(
                    "INSERT INTO actors(name,surname) VALUES (?, ?)");

            ps.setString(1, name);
            ps.setString(2, surname);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void showActors () {

        try {
            ps = conn.prepareStatement("SELECT * FROM actors");
            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getInt("id_actor")
                        + ". Imie aktora: " + rs.getString("name")
                        + ", Nazwisko aktora: " + rs.getString("surname")
                        + ", Wiek aktora: " + rs.getInt("age"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void showMoviesAndActors () {

        try {
            ps = conn.prepareStatement(
                    "SELECT ma.id_movie, m.title, m.description, m.duration, ma.id_actor, a.name, a.surname, a.age "
                            + "FROM movie_actors AS ma, movies AS m, actors AS a "
                            + "WHERE m.id_movie = ma.id_movie AND a.id_actor = ma.id_actor");
            rs = ps.executeQuery();

            while (rs.next()) {

                System.out.println("id: " + rs.getInt("ma.id_movie")
                        + ", Tytuł: " + rs.getString("m.title")
                        + ", Opis: " + rs.getString("m.description")
                        + ", Czas trwania: " + rs.getString("m.duration")
                        + ". Id: " + rs.getInt("ma.id_actor")
                        + ", Imie: " + rs.getString("a.name")
                        + ", Nazwisko: " + rs.getString("a.surname")
                        + ", Wiek: " + rs.getInt("a.age"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

    }


    public void addMovieGenre (int id_movie, int id_genre) {

        try {

            ps = conn.prepareStatement(
                    "INSERT INTO movie_genres(id_movie, id_genre) VALUES (?,?)");
            ps.setInt(1, id_movie);
            ps.setInt(2, id_genre);
            ps.execute();

        } catch (SQLException e) { e.printStackTrace();
        } finally {
            close();
        }
    }

    public void showMoviesAndGenres () {

        try {
            ps = conn.prepareStatement(
                    "SELECT mv.id_movie, m.title, m.description, m.duration, mv.id_genre, g.type_genre " +
                            "FROM movies AS m, genres as g, movie_genres AS mv " +
                            "WHERE m.id_movie = mv.id_movie AND g.id_genre = mv.id_genre ");
            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("Id: " + rs.getInt("mv.id_movie")
                        + ", Tytuł: " + rs.getString("m.title")
                        + ", Opis: " + rs.getString("m.description")
                        + ". Czas trwania: " + rs.getInt("m.duration")
                        + ", Id: " + rs.getInt("mv.id_genre")
                        + ", Typ gatunku: " + rs.getString("g.type_genre"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void addDirectors (String name, String surname) {

        try {
            ps = conn.prepareStatement(
                    "INSERT INTO directors(name,surname) VALUES (?,?)");
            ps.setString(1, name);
            ps.setString(2, surname);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

    }
    public List<Movie> getMovies() {

        sql = "select * from movies";
        List<Movie> movies = new ArrayList<>();

        try {
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
            while (rs.next()) {
                movies.add(new Movie(
                        rs.getInt("id_movie"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("duration")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return movies;
    }

    public void showDirectors () {

        try {
            ps = conn.prepareStatement("SELECT * FROM directors");
            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("id: " + rs.getInt("id_director")
                        + ", imie: " + rs.getString("nanme")
                        + ", nazwisko: " + rs.getString("surname"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void addMoviesAndDirectors (int id_movie, int id_director) {

        try {
            ps = conn.prepareStatement("INSERT INTO movie_directors VALUES (?,?)");
            ps.setInt(1, id_movie);
            ps.setInt(2, id_director);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

    }

    public void showMoviesAndDirectors () {

        try {
            ps = conn.prepareStatement(
                    "SELECT md.id_movie, m.title, m.description, m.duration, md.id_director, d.name, d.surname " +
                            "FROM movie_directors AS md, movies AS m, directors AS d " +
                            "WHERE md.id_movie = m.id_movie AND md.id_director = d.id_director");
            rs = ps.executeQuery();

            while (rs.next()) {

                System.out.println("id: " + rs.getInt("md.id_movie")
                        + ", Tytul filmu: " + rs.getString("m.title")
                        + ", Opis filmu: " + rs.getString("m.description")
                        + ", Czas trwania: " + rs.getString("m.duration")
                        + ". id: " + rs.getString("md.id_director")
                        + ", Imie: " + rs.getString("d.name")
                        + ", Nazwisko: " + rs.getString("d.surname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void showUsers () {

        try {
            ps = conn.prepareStatement("SELECT * FROM users");
            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("id: " + rs.getInt("id_user")
                        + ", Username: " + rs.getString("username")
                        + ", Password: " + rs.getString("password")
                        + ", email: " + rs.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void showEmployees () {

        try {
            ps = conn.prepareStatement("SELECT * FROM employee");
            rs = ps.executeQuery();

            while (rs.next()) {

                System.out.println("id: " + rs.getInt("id_employee")
                        + ", Name: " + rs.getString("name")
                        + ", surname: " + rs.getString("surname")
                        + ", username: " + rs.getString("username")
                        + ", password: " + rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public List<Seat> getSeatsTakenForSeance(Seance seance) {
        sql = "select seat, row from tickets where id_seance = ?";

        List<Seat> seats = new ArrayList<>();

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, seance.getId());
            rs = ps.executeQuery();

            while (rs.next()) {
                seats.add(new Seat(
                        rs.getInt("seat"),
                        rs.getInt("row")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seats;
    }

    public void addOrder(Order order) {
        sql = "insert into orders (id_order, id_seance, id_user, id_employee) values (NULL, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getSeanceId());
            ps.setInt(2, order.getUserId());
            ps.setInt(3, order.getEmployeeId());

            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);

                for (Ticket t : order.getTickets()) {
                    sql = "insert into tickets (id_ticket, id_seance, ticket_type, row, seat) values (NULL, ?, ?, ?, ?)";

                    ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, order.getSeanceId());
                    ps.setString(2, t.getTicketType());
                    ps.setInt(3, t.getRow());
                    ps.setInt(4, t.getSeat());
                    ps.executeUpdate();

                    rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        int ticketId = rs.getInt(1);

                        sql = "insert into order_tickets (id_order, id_ticket) values (?, ?)";

                        ps = conn.prepareStatement(sql);
                        ps.setInt(1, orderId);
                        ps.setInt(2, ticketId);
                        ps.executeUpdate();
                    } else {
                        conn.rollback();
                    }

                }

                conn.commit();

            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            close();
        }
    }
//
//    public void addEmployee (String name, String surname, String username, String password) {
//
//        sql = "INSERT INTO employee(name, surname, username, password) VALUES (?,?,?,?)";
//
//        try {
//            ps = conn.prepareStatement(sql);
//            ps.setString(1, name);
//            ps.setString(2, surname);
//            ps.setString(3, username);
//            ps.setString(4, password);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }




    }







