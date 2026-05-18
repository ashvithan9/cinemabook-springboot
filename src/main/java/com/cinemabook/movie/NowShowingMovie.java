package com.cinemabook.movie;



/**
 * A movie currently showing in theatres — can be booked.
 * Demonstrates: Inheritance, Polymorphism
 */
public class NowShowingMovie extends AbstractMovie {

    public NowShowingMovie(String movieId, String title, String genre,
                           String duration, String rating,
                           String description, String imageUrl) {
        super(movieId, title, genre, duration, rating, description, imageUrl);
    }

    @Override public String getStatus()      { return "NOW_SHOWING"; }
    @Override public boolean isBookable()    { return true; }
    @Override public String getStatusLabel() { return "Now Showing"; }
    @Override public String getBadgeStyle()  { return "background:#C9A84C;color:#000"; }
}
