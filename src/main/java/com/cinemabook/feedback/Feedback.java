package com.cinemabook.feedback;

public class Feedback {

    private int id;
    private String username;
    private String movieTitle;
    private int rating;
    private String comment;
    private String date;
    private int likes;
    private String sentiment;

    // Constructor
    public Feedback(int id, String username, String movieTitle,
                    int rating, String comment, String date, int likes) {
        this.id = id;
        this.username = username;
        this.movieTitle = movieTitle;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.likes = likes;
        this.sentiment = (rating >= 3) ? "POSITIVE" : "NEGATIVE";
    }

    // Getters
    public int getId()           { return id; }
    public String getUsername()  { return username; }
    public String getMovieTitle(){ return movieTitle; }
    public int getRating()       { return rating; }
    public String getComment()   { return comment; }
    public String getDate()      { return date; }
    public int getLikes()        { return likes; }
    public String getSentiment() { return sentiment; }

    // Setters
    public void setId(int id)               { this.id = id; }
    public void setUsername(String u)       { this.username = u; }
    public void setMovieTitle(String m)     { this.movieTitle = m; }
    public void setRating(int r)            { this.rating = r; this.sentiment = (r >= 3) ? "POSITIVE" : "NEGATIVE"; }
    public void setComment(String c)        { this.comment = c; }
    public void setDate(String d)           { this.date = d; }
    public void setLikes(int l)             { this.likes = l; }
}
