package com.cinemabook.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // View all bookings (Admin view)
    @GetMapping
    public String viewAllBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        // Simulating logged-in user for navigation bar
        model.addAttribute("userRole", "ADMIN");
        model.addAttribute("userName", "Admin User");
        return "booking/list";
    }

    // View user's bookings (Customer view)
    @GetMapping("/my")
    public String viewMyBookings(Model model) {
        // Hardcoding user ID for now since auth is not set up
        String loggedInUserId = "U1";
        model.addAttribute("bookings", bookingService.getBookingsByUserId(loggedInUserId));
        
        model.addAttribute("userRole", "CUSTOMER");
        model.addAttribute("userName", "Customer User");
        return "booking/list";
    }

    // Show booking details
    @GetMapping("/{id}")
    public String viewBookingDetail(@PathVariable String id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            return "redirect:/bookings/my";
        }
        model.addAttribute("booking", booking);
        model.addAttribute("userRole", "CUSTOMER");
        model.addAttribute("userName", "Customer User");
        return "booking/detail";
    }

    // Show form to create new booking
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Booking newBooking = new Booking();
        // pre-fill for demo purposes
        newBooking.setUserId("U1");
        newBooking.setMovieTitle("Select a Movie");
        newBooking.setSeats(1);
        
        model.addAttribute("booking", newBooking);
        model.addAttribute("userRole", "CUSTOMER");
        model.addAttribute("userName", "Customer User");
        return "booking/form";
    }

    // Save a new booking
    @PostMapping
    public String saveBooking(@ModelAttribute("booking") Booking booking) {
        // Calculate price (mock: $10 per seat)
        booking.setTotalPrice(booking.getSeats() * 10.0);
        bookingService.createBooking(booking);
        return "redirect:/bookings/my";
    }

    // Cancel a booking
    @GetMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable String id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking != null) {
            booking.setStatus("CANCELLED");
            bookingService.updateBooking(booking);
        }
        return "redirect:/bookings/my";
    }
    
    // Delete a booking (Admin only usually, but open for demo)
    @GetMapping("/{id}/delete")
    public String deleteBooking(@PathVariable String id) {
        bookingService.deleteBooking(id);
        return "redirect:/bookings/my";
    }
}
