package com.cinemabook.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private String bookingId;
    private String userId;
    private String movieId;
    private String movieTitle;
    private String showTime;
    private Integer seats;
    private String seatNumbers;
    private Double totalPrice;
    private String status; // CONFIRMED, CANCELLED
}
