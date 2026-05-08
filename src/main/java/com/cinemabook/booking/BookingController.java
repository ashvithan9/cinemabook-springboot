package com.cinemabook.booking;

import com.cinemabook.booking.Booking;
import com.cinemabook.booking.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/add")
    public String addBooking(@RequestBody Booking booking) {
        return bookingService.addBooking(booking);
    }

    @GetMapping("/all")
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }
}

