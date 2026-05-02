package com.cinemabook.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private String movieId;
    private String title;
    private String genre;
    private String duration;
    private String rating;
    private String description;
    private String imageUrl;
    private String status; // NOW_SHOWING or COMING_SOON
}


