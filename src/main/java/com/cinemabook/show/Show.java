package com.cinemabook.show;

public class Show {

    private String showId;
    private String movieId;
    private String date;
    private String time;
    private String hall;
    private int totalSeats;
    private int availableSeats;

    public Show() {
    }

    public Show(String showId, String movieId, String date,
                String time, String hall,
                int totalSeats, int availableSeats) {

        this.showId = showId;
        this.movieId = movieId;
        this.date = date;
        this.time = time;
        this.hall = hall;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}


