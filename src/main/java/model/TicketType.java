package model;

public class TicketType {

    private String ticketType;
    private int price;

    public TicketType(String ticketType, int price) {
        this.ticketType = ticketType;
        this.price = price;
    }

    public String getTicketType() {
        return ticketType;
    }

    public int getPrice() {
        return price;
    }
}
