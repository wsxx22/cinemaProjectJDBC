package model;

public class Ticket {

    private int ticketId;
    private int seanceId;
    private String ticketType;
    private int row;
    private int seat;

    public Ticket(int ticketId, int seanceId, String ticketType, int row, int seat) {
        this.ticketId = ticketId;
        this.seanceId = seanceId;
        this.ticketType = ticketType;
        this.row = row;
        this.seat = seat;
    }

    public Ticket(int seanceId, String ticketType, int row, int seat) {
        this.seanceId = seanceId;
        this.ticketType = ticketType;
        this.row = row;
        this.seat = seat;
    }

    public int getTicketId() {
        return ticketId;
    }

    public int getSeanceId() {
        return seanceId;
    }

    public String getTicketType() {
        return ticketType;
    }

    public int getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }
}
