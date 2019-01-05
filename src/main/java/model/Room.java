package model;

public class Room {

    private int roomId;
    private String name;
    private int seats;
    private int rows;

    public Room(int roomId, String name, int seats, int rows) {
        this.roomId = roomId;
        this.name = name;
        this.seats = seats;
        this.rows = rows;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
