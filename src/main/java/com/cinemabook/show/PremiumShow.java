package com.cinemabook.show;

/**
 * A VIP/premium show — 20% surcharge applied.
 * Demonstrates: Inheritance, Polymorphism
 */
public class PremiumShow extends AbstractShow {

    private static final double PREMIUM_SURCHARGE = 1.20;

    public PremiumShow(String showId, String movieId, String movieTitle,
                       String date, String time, String hall,
                       int totalSeats, int availableSeats, String price) {
        super(showId, movieId, movieTitle, date, time, hall,
              totalSeats, availableSeats, price);
    }

    @Override
    public double calculateTotalPrice(int seats) {
        return seats * Double.parseDouble(price) * PREMIUM_SURCHARGE;
    }

    @Override
    public String getShowType() { return "Premium"; }

    @Override
    public String getHallDescription() {
        return hall + " — Premium VIP screening (+20% surcharge)";
    }
}
