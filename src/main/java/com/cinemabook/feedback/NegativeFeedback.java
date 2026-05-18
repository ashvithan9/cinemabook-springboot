package com.cinemabook.feedback;

/**
 * Negative feedback (rating < 3).
 * Demonstrates: Inheritance, Polymorphism
 */
public class NegativeFeedback extends AbstractFeedback {

    public NegativeFeedback(int id, String username, String movieTitle,
                            int rating, String comment, String date, int likes) {
        super(id, username, movieTitle, rating, comment, date, likes);
    }

    @Override public String getSentiment()           { return "NEGATIVE"; }
    @Override public String getSentimentIcon()       { return "👎"; }
    @Override public String getSentimentBadgeStyle() { return "background:#4a1a1a;color:#e84a4a"; }
    @Override public String getRecommendationText()  { return "Not Recommended"; }
}
