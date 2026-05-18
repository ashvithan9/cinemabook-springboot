package com.cinemabook.show;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Show {
    private String showId;
    private String movieId;
    private String movieTitle;
    private String date;
    private String time;
    private String hall;
    private int totalSeats;
    private int availableSeats;
    private String price;
}
