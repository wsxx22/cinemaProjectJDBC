package model;

import java.time.LocalDateTime;

public class Seance {

    private int id;
    private int movieId;
    private int roomId;
    private LocalDateTime localDateTime;

    public Seance(int id, int movieId, int roomId, LocalDateTime localDateTime) {
        this.id = id;
        this.movieId = movieId;
        this.roomId = roomId;
        this.localDateTime = localDateTime;
    }

    public Seance(int movieId, int roomId, LocalDateTime localDateTime) {
        this.movieId = movieId;
        this.roomId = roomId;
        this.localDateTime = localDateTime;
    }

    public int getId() {
        return id;
    }

    public int getMovieId() {
        return movieId;
    }

    public int getRoomId() {
        return roomId;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}
