package com.cinemabook.show;

/**
 * Abstract base class for all show types.
 * Demonstrates: Abstraction, Encapsulation, Information Hiding
 */
public abstract class AbstractShow {

    protected String showId;
    protected String movieId;
    protected String movieTitle;
    protected String date;
    protected String time;
    protected String hall;
    protected int totalSeats;
    protected int availableSeats;
    protected String price;

    public AbstractShow(String showId, String movieId, String movieTitle,
                        String date, String time, String hall,
                        int totalSeats, int availableSeats, String price) {
        this.showId         = showId;
        this.movieId        = movieId;
        this.movieTitle     = movieTitle;
        this.date           = date;
        this.time           = time;
        this.hall           = hall;
        this.totalSeats     = totalSeats;
        this.availableSeats = availableSeats;
        this.price          = price;
    }

    // Abstraction — each show type defines its pricing rules
    public abstract double calculateTotalPrice(int seats);
    public abstract String getShowType();
    public abstract String getHallDescription();

    // Information Hiding — capacity check is internal logic
    public boolean hasAvailability(int requestedSeats) {
        return availableSeats >= requestedSeats;
    }

    public boolean isSoldOut() {
        return availableSeats == 0;
    }

    // Getters
    public String getShowId()        { return showId; }
    public String getMovieId()       { return movieId; }
    public String getMovieTitle()    { return movieTitle; }
    public String getDate()          { return date; }
    public String getTime()          { return time; }
    public String getHall()          { return hall; }
    public int getTotalSeats()       { return totalSeats; }
    public int getAvailableSeats()   { return availableSeats; }
    public String getPrice()         { return price; }
}
