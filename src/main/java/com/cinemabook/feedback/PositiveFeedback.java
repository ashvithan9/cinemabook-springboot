package com.cinemabook.feedback;

/**
 * Positive feedback (rating >= 3).
 * Demonstrates: Inheritance, Polymorphism
 */
public class PositiveFeedback extends AbstractFeedback {

    public PositiveFeedback(int id, String username, String movieTitle,
                            int rating, String comment, String date, int likes) {
        super(id, username, movieTitle, rating, comment, date, likes);
    }

    @Override public String getSentiment()           { return "POSITIVE"; }
    @Override public String getSentimentIcon()       { return "👍"; }
    @Override public String getSentimentBadgeStyle() { return "background:#1a4a1a;color:#4ade80"; }
    @Override public String getRecommendationText()  { return "Recommended"; }
}
