package com.cinemabook.feedback;

/**
 * Abstract base class for all feedback types.
 * Demonstrates: Abstraction, Encapsulation, Information Hiding
 */
public abstract class AbstractFeedback {

    protected int id;
    protected String username;
    protected String movieTitle;
    protected int rating;
    protected String comment;
    protected String date;
    protected int likes;

    public AbstractFeedback(int id, String username, String movieTitle,
                            int rating, String comment, String date, int likes) {
        this.id         = id;
        this.username   = username;
        this.movieTitle = movieTitle;
        this.rating     = rating;
        this.comment    = comment;
        this.date       = date;
        this.likes      = likes;
    }

    // Abstraction — sentiment-specific behaviour
    public abstract String getSentiment();
    public abstract String getSentimentIcon();
    public abstract String getSentimentBadgeStyle();
    public abstract String getRecommendationText();

    // Information Hiding — star display logic is internal
    public String getStarDisplay() {
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }

    public boolean isPopular() { return likes >= 5; }

    // Getters & Setters
    public int getId()            { return id; }
    public String getUsername()   { return username; }
    public String getMovieTitle() { return movieTitle; }
    public int getRating()        { return rating; }
    public String getComment()    { return comment; }
    public String getDate()       { return date; }
    public int getLikes()         { return likes; }
    public void setLikes(int l)   { this.likes = l; }
    public void setId(int id)     { this.id = id; }
}
