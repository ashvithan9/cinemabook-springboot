package com.cinemabook.booking;

import com.cinemabook.booking.Booking;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final String FILE_PATH = "src/main/resources/data/bookings.txt";

    public String addBooking(Booking booking) {
        if (!booking.isValidBooking()) {
            return "Invalid booking details";
        }

        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            file.createNewFile();

            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true));
            bw.write(booking.toFileString());
            bw.newLine();
            bw.close();

            return "Booking added successfully";

        } catch (IOException e) {
            e.printStackTrace();
            return "Error saving booking";
        }
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();

        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            file.createNewFile();

            BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
            String line;

            while ((line = br.readLine()) != null) {
                Booking booking = Booking.fromFileString(line);

                if (booking != null) {
                    bookings.add(booking);
                }
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookings;
    }
}

