package com.cinemabook.show;

/**
 * A standard cinema show — flat price per seat.
 * Demonstrates: Inheritance, Polymorphism
 */
public class RegularShow extends AbstractShow {

    public RegularShow(String showId, String movieId, String movieTitle,
                       String date, String time, String hall,
                       int totalSeats, int availableSeats, String price) {
        super(showId, movieId, movieTitle, date, time, hall,
              totalSeats, availableSeats, price);
    }

    @Override
    public double calculateTotalPrice(int seats) {
        return seats * Double.parseDouble(price);
    }

    @Override
    public String getShowType() { return "Regular"; }

    @Override
    public String getHallDescription() {
        return hall + " — Standard screening";
    }
}
