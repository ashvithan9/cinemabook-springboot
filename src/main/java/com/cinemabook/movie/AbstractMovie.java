package com.cinemabook.movie;




/**
 * Abstract base class for all movie types.
 * Demonstrates: Abstraction, Encapsulation, Information Hiding
 */
public abstract class AbstractMovie {

    // Encapsulation — protected fields
    protected String movieId;
    protected String title;
    protected String genre;
    protected String duration;
    protected String rating;
    protected String description;
    protected String imageUrl;

    public AbstractMovie(String movieId, String title, String genre,
                         String duration, String rating,
                         String description, String imageUrl) {
        this.movieId     = movieId;
        this.title       = title;
        this.genre       = genre;
        this.duration    = duration;
        this.rating      = rating;
        this.description = description;
        this.imageUrl    = imageUrl;
    }

    // Abstraction — subclasses define these
    public abstract String getStatus();
    public abstract boolean isBookable();
    public abstract String getStatusLabel();
    public abstract String getBadgeStyle();

    // Shared concrete method
    public String getSummary() {
        return title + " | " + genre + " | " + duration + " min | ★ " + rating;
    }

    // Getters
    public String getMovieId()    { return movieId; }
    public String getTitle()      { return title; }
    public String getGenre()      { return genre; }
    public String getDuration()   { return duration; }
    public String getRating()     { return rating; }
    public String getDescription(){ return description; }
    public String getImageUrl()   { return imageUrl; }
}
