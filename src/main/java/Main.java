import model.*;
import model.account.Employee;
import model.account.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.System.exit;

public class Main {

    enum Menu {
        MAIN, CLIENT, CLIENT_LOGGED, EMPLOYEE_LOGGED, EXIT
    }

    private static Cinema cinema;
    private static User user;

    private static Scanner scanner;
    private static Menu menu;

    public static void main(String[] args) {
        cinema = new Cinema();
        scanner = new Scanner(System.in);
        menu = Menu.MAIN;

        Actor[] actors = new Actor[50];
        actors[0] = new Actor(1, "Jan", "Kowalski");
        actors[1] = new Actor(2, "Stefan", "Brzeczek");

        for (Actor a : actors) {
            if (a instanceof Actor)
            System.out.println(a.getId() + " " + a.getName() + " " + a.getSurname());
            else
                break;
        }



//        while (menu != Menu.EXIT) {
//            fakeClearScreen();
//            handleMenu();
//        }

//        LocalDateTime localDateTime = LocalDateTime.of(2000,12,10,20,10);
//        LocalDate localDate2 = localDateTime.toLocalDate();
//        LocalDate localDate = LocalDate.of(2018,10,3);
//        String b = "2000-12-10 13:20:00";
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate localDate5 = LocalDate.parse(b.substring(0,10),dateTimeFormatter);
//
//        Database database = new Database();
//        for (SeanceSelected ss : database.getSeanceToOrder(localDate)) {
//            System.out.println(ss.getId() + " " + ss.getTitle() + " " + ss.getRoomName());
//        }

    }

    public static void handleMenu() {
        switch (menu) {
            case MAIN:
                handleMainMenu();
                break;
            case CLIENT:
                handleClientMenu();
                break;
            case CLIENT_LOGGED:
                handleClientLoggedMenu();
                break;
            case EMPLOYEE_LOGGED:
                handleEmployeeLogMenu();
                break;
            case EXIT:
                exit (0);
        }
    }

    private static void handleMainMenu() {
        System.out.println("1. Jestem klientem");
        System.out.println("2. Jestem pracownikiem");
        System.out.println("3. Zakończ");

        switch (scanner.nextInt()) {
            case 1:
                menu = Menu.CLIENT;
                break;
            case 2:
                logInEmployee();
                break;
            case 3:
                menu = Menu.EXIT;
                break;
            default:
                menu = Menu.MAIN;
                System.out.println("Wybierz poprawny numer");
                break;
        }
    }

    private static void handleClientMenu() {
        System.out.println("1. Zaloguj się");
        System.out.println("2. Zarejestruj się");
        System.out.println("3. Kup bilet bez rejestracji");
        System.out.println("4. Wstecz");
        System.out.println("5. Zakończ");

        switch (scanner.nextInt()) {
            case 1:
                logInClient();
                break;
            case 2:
                handleClientRegister();
                break;
            case 3:
                handleBuyTicket(0);
            case 4:
                menu = Menu.MAIN;
                break;
            case 5:
                menu = Menu.EXIT;
                break;
            default:
                System.out.println("Wybierz poprawny numer");
                break;
        }
    }

    private static void logInClient() {
        System.out.print("1. Podaj login: ");
        String username = scanner.next();
        System.out.print("2. Podaj hasło: ");
        String password = scanner.next();

        user = cinema.loginUser(username, password);
        if (user != null) {
            System.out.println("Zalogowano");
            menu = Menu.CLIENT_LOGGED;

        } else {
            System.out.println("Popraw dane");
        }
    }

    private static void handleClientRegister() {
        System.out.print("Podaj nazwe uzytkownika: ");
        String username = scanner.next();
        System.out.print("Podaj haslo: ");
        String password = scanner.next();
        System.out.println("Podaj email: ");
        String email = scanner.next();

        user = cinema.userRegister(username, password, email);
        if (user != null) {
            System.out.println("Konto utworzone.");
        } else {
            System.out.println("Uzytkownik o podanych danych juz istnieje.");
        }
    }

    private static void handleClientLoggedMenu() {

        System.out.println("1. Kup bilet");
        System.out.println("2. Bilety kupione");
        System.out.println("3. Wyloguj");
        System.out.println("4. Wyjście całkowite");

        switch (scanner.nextInt()) {
            case 1:
                handleBuyTicket(user.getId());
                menu = Menu.CLIENT_LOGGED;
                break;
            case 2:
                System.out.println("Kupione bilety:");
               cinema.showBoughtUserTickets(user.getUsername());
                menu = Menu.CLIENT_LOGGED;
                break;
            case 3:
                menu = Menu.CLIENT;
                break;
            case 4:
                menu = Menu.EXIT;
                break;
        }
    }

    private static void handleBuyTicket(int userId) {
        LocalDate localDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("Wyświetl repertuar na dany dzien.");
        System.out.println("1. Dziś");
        System.out.println("2. Jutro");
        System.out.println("3. Inny dzien");
        System.out.println("4. Wyjście");

        switch (scanner.nextInt()) {
            case 1: localDate = LocalDate.now(); break;
            case 2: localDate = LocalDate.now().plusDays(1); break;
            case 3:
                System.out.print("Podaj dzien (yyyy-mm-dd):");
                String localDateString = scanner.next().trim();
                localDate = LocalDate.parse(localDateString, formatter);
                break;
            case 4: menu = Menu.CLIENT_LOGGED; break;
            default: System.out.println("Podaj poprawny numer"); break;
        }

        Map<Seance, Movie> seances = cinema.getSeanceToOrder(localDate);
        if (seances.isEmpty()) {
            return;
        }

        seances.forEach((s, m) -> {
            System.out.println("id: " + s.getId() + ", Tytuł: " + m.getTitle() + ", Opis: " + m.getDescription()
                    + ", Czas trwania: " + m.getDuration() + ", Numer sali: " + s.getRoomId()
                    + ", Czas rozpoczęcia: " + s.getLocalDateTime());
        });

        Optional<Seance> optionalSeance = Optional.empty();
        int seanceId = 0;
        while (!optionalSeance.isPresent()) {
            System.out.print("Podaj id seansu: ");
            seanceId = scanner.nextInt();
            int finalSeanceId = seanceId;
            optionalSeance = seances.keySet().stream().filter(s -> s.getId() == finalSeanceId).findFirst();
        }

        List<TicketType> ticketTypeList = cinema.showTicketTypes();
        ticketTypeList.forEach(t -> System.out.println("Typ biletu: " + t.getTicketType() + ", Cena:" + t.getPrice()));

        Optional<TicketType> optionalTicketType = Optional.empty();
        String ticketType = null;
        while (!optionalTicketType.isPresent()) {
            System.out.println("Podaj typ biletu");
            ticketType = scanner.next().trim();
            String finalTicketType = ticketType;
            optionalTicketType = ticketTypeList.stream().filter(t -> t.getTicketType().equals(finalTicketType)).findFirst();
        }

        Seance seance = optionalSeance.get();
        Room room = cinema.getRooms().stream().filter(r -> r.getRoomId() == seance.getRoomId()).findFirst().get();

        char[][] seats = new char[room.getRows()][room.getSeats()];
        for (int i = 0; i < room.getRows(); i++) {
            for (int j = 0; j < room.getSeats(); j++) {
                seats[i][j] = ' ';
            }
        }

        List<Seat> seatsTaken = cinema.getSeatsTakenForSeance(seance);
        for (Seat s : seatsTaken) {
            seats[s.getRow() - 1][s.getSeat() - 1] = 'Z';
        }

        Order order = new Order(seance.getId(), userId, 0, LocalDateTime.now());

        while (true) {
            for (int i = 0; i < room.getRows(); i++) {
                System.out.print(i+1 + "");

                for (int j = 0; j < room.getSeats(); j++) {
                    System.out.print("[" + seats[i][j] + "] ");
                }
                System.out.println();
            }

            int row, seat;
            while (true) {
                System.out.print("Rząd: ");
                row = scanner.nextInt();
                System.out.print("Miejsce: ");
                seat = scanner.nextInt();

                if (seats[row - 1][seat - 1] == 'Z') {
                    System.out.println("To miejsce jest zajete");
                } else {
                    seats[row - 1][seat - 1] = 'Z';
                    break;
                }
            }

            order.getTickets().add(new Ticket(seance.getId(), ticketType, row, seat));

            System.out.println("Zarezerwuj więcej miejsc T/N");
            if (scanner.next().trim().charAt(0) == 'N') break;
        }

        cinema.addOrder(order);
    }

    private static void logInEmployee() {
        System.out.println("Podaj login: ");
        String username = scanner.next();

        System.out.println("Podaj hasło: ");
        String password = scanner.next();

        Employee employee = cinema.loginEmployee(username, password);

        if (employee != null) {
            menu = Menu.EMPLOYEE_LOGGED;
            System.out.println("Zalogowano");
        }
         else {
            System.out.println("Popraw dane");
         }
    }

    private static void handleEmployeeLogMenu() {
        System.out.println("1. Dodaj film");
        System.out.println("2. Dodaj seans");
        System.out.println("3. Dodaj typ biletu");
        System.out.println("4. Dodaj sale");
        System.out.println("5. Wyjście");

        switch (scanner.nextInt()) {
            case 1:
                handleAddMovie();
                break;
            case 2:
                addSeance();
                break;
            case 3:
                addTypeTicket();
                break;
            case 4:
                addRoom();
            case 5:
                menu = Menu.EXIT;
                break;

        }

    }

    private static void addSeance() {
        List<Movie> movies = cinema.getMovies();

        movies.forEach(m -> System.out.println(m.getId() + " " + m.getTitle() + " " + m.getDuration()));

        Optional<Movie> optionalMovie = Optional.empty();

        while (!optionalMovie.isPresent()) {
            System.out.print("Podaj id filmu: ");
            int movieId = scanner.nextInt();

            optionalMovie = movies.stream().filter(m -> m.getId() == movieId).findFirst();
        }

        List<Room> rooms = cinema.getRooms();
        rooms.forEach(r -> System.out.println(r.getRoomId() + " nazwa sali: " + r.getName() + " "  +  r.getSeats()));

        Optional<Room> optionalRoom = Optional.empty();
        while (!optionalRoom.isPresent()) {
            System.out.print("Podaj id sali: ");
            int roomId = scanner.nextInt();

            optionalRoom = rooms.stream().filter(r -> r.getRoomId() == roomId).findFirst();
        }

        String datePattern = "dd.MM.yyyy";
        System.out.println("Podaj datę: " + datePattern);
        LocalDate date = LocalDate.parse(scanner.next(), DateTimeFormatter.ofPattern(datePattern));

        String timePattern = "hh:mm";
        System.out.println("Podaj godzinę rozpoczęcia : "+ timePattern);
        LocalTime time = LocalTime.parse(scanner.next(), DateTimeFormatter.ISO_LOCAL_TIME);

        LocalDateTime dateTime = LocalDateTime.of(date, time);

//        System.out.print("Podaj czas w formacie yyyy-mm-dd hh:mm : ");
////        String dateTimeSeance = scanner.next();
//        String dateTimeSeance = "2018-01-01 12:30";
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeSeance, dateTimeFormatter);
//        System.out.println(localDateTime);

        Seance seance = new Seance(optionalMovie.get().getId(), optionalRoom.get().getRoomId(), dateTime);
        if (cinema.isSeanceColliding(seance)) {
            cinema.addSeance(seance);
            System.out.println("dodane seans");
        } else {
            System.out.println("nie mozna dodac seansu");
        }
    }

    private static void addTypeTicket() {

        System.out.print("Podaj typ biletu: ");
        String type = scanner.next();
        System.out.print("Podaj cene biletu: ");
        int price = scanner.nextInt();

        TicketType ticketType = cinema.addTicketType(type, price);

        if (ticketType != null) {
            System.out.println("Udało się");
        } else {
            System.out.println("nie udalo sie");
        }

    }

    private static void handleAddMovie() {

        System.out.print("Podaj tytlu filmi: ");
        String title = scanner.next();

        System.out.print("Podaj opis filmu: ");
        String description = scanner.next();

        System.out.print("Podaj czas trwania filmu: ");
        int duration = scanner.nextInt();


        String name, surname;
        List<Director> directors = new ArrayList<>();
        while (true) {
            System.out.print("Podaj imie rezysera: ");
            name = scanner.next();

            System.out.print("Podaj nazwisko rezysera: ");
            surname = scanner.next();

            directors.add(new Director(name, surname));

            System.out.println("Czy chcesz dodac kolejnego rezysera? (t/n)");
            if (scanner.next().charAt(0) == 'n') break;
        }

        String genre;
        List<String> genres = new ArrayList<>();
        while (true) {
            System.out.print("Podaj gatunek filmowy: ");
            genre = scanner.next();

            genres.add(genre);

            System.out.println("Czy chcesz dodac kolejny gatunek filmowy? (t/n)");
            if (scanner.next().charAt(0) == 'n') break;
        }

        List<Actor> actors = new ArrayList<>();
        while (true) {
            System.out.print("Podaj imie aktora: ");
            name = scanner.next();

            System.out.print("Podaj nazwisko aktora: ");
            surname = scanner.next();

            actors.add(new Actor(name, surname));

            System.out.println("Czy chcesz dodac kolejnego aktora? (t/n)");
            if (scanner.next().charAt(0) == 'n') break;
        }

        Movie movie = new Movie(title, description, duration, directors, genres, actors);

        if (cinema.addMovie(movie)) {
            System.out.println("Dodano film");
        } else {
            System.out.println("Nie udalo sie dodac filmu");
        }

    }

    private static void addRoom() {

        List<Room> rooms = new ArrayList<>();
        System.out.print("Podaj nazwe sali: ");
        String name = scanner.next();

        System.out.print("Podaj ilość miejsc w rzedzie: ");
        int seats = scanner.nextInt();

        System.out.print("Podaj ilość rzedow: ");
        int rows = scanner.nextInt();

        Room room = cinema.addRoom(name, seats, rows);

        if (room != null) {
            rooms.add(room);
            System.out.println("Dodano sale.");
        } else {
            System.out.println("Nie dodano sali.");
        }

    }

    private static void fakeClearScreen() {
        for (int i =0; i<10; i++) {
            System.out.println();
        }
    }



}
