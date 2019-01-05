package model;

public class Seat {
    private int seat;
    private int row;

    public Seat(int seat, int row) {
        this.seat = seat;
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}

