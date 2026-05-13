package com.cinemabook.booking;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    
    private final List<Booking> bookings = new ArrayList<>();
    
    public BookingService() {
        // Add a mock booking for testing
        bookings.add(new Booking(
                UUID.randomUUID().toString(),
                "U1", // mock user ID
                "M1", // mock movie ID
                "Dune: Part Two",
                "2026-05-20 18:00",
                2,
                "A1, A2",
                20.0,
                "CONFIRMED"
        ));
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }
    
    public List<Booking> getBookingsByUserId(String userId) {
        return bookings.stream()
                .filter(b -> b.getUserId().equals(userId))
                .toList();
    }

    public Booking getBookingById(String bookingId) {
        return bookings.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }

    public void createBooking(Booking booking) {
        booking.setBookingId(UUID.randomUUID().toString());
        booking.setStatus("CONFIRMED");
        bookings.add(booking);
    }

    public void updateBooking(Booking updatedBooking) {
        Booking existingBooking = getBookingById(updatedBooking.getBookingId());
        if (existingBooking != null) {
            existingBooking.setMovieId(updatedBooking.getMovieId());
            existingBooking.setMovieTitle(updatedBooking.getMovieTitle());
            existingBooking.setShowTime(updatedBooking.getShowTime());
            existingBooking.setSeats(updatedBooking.getSeats());
            existingBooking.setSeatNumbers(updatedBooking.getSeatNumbers());
            existingBooking.setTotalPrice(updatedBooking.getTotalPrice());
            existingBooking.setStatus(updatedBooking.getStatus());
        }
    }

    public void deleteBooking(String bookingId) {
        bookings.removeIf(b -> b.getBookingId().equals(bookingId));
    }
}
