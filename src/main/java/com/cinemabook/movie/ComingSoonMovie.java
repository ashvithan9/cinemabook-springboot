package com.cinemabook.movie;



/**
 * An upcoming movie — not yet bookable.
 * Demonstrates: Inheritance, Polymorphism
 */
public class ComingSoonMovie extends AbstractMovie {

    public ComingSoonMovie(String movieId, String title, String genre,
                           String duration, String rating,
                           String description, String imageUrl) {
        super(movieId, title, genre, duration, rating, description, imageUrl);
    }

    @Override public String getStatus()      { return "COMING_SOON"; }
    @Override public boolean isBookable()    { return false; }
    @Override public String getStatusLabel() { return "Coming Soon"; }
    @Override public String getBadgeStyle()  { return "background:#1a1a1a;color:#555"; }
}
