
package com.cinemabook.booking;

public class Booking {
    private String bookingId;
    private String customerName;
    private String movieName;
    private String seatNumber;
    private String showTime;
    private double price;

    public Booking() {}

    public Booking(String bookingId, String customerName, String movieName,
                   String seatNumber, String showTime, double price) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.movieName = movieName;
        this.seatNumber = seatNumber;
        this.showTime = showTime;
        this.price = price;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isValidBooking() {
        return bookingId != null && !bookingId.isEmpty()
                && customerName != null && !customerName.isEmpty()
                && movieName != null && !movieName.isEmpty()
                && seatNumber != null && !seatNumber.isEmpty()
                && showTime != null && !showTime.isEmpty()
                && price > 0;
    }

    public String toFileString() {
        return bookingId + "," + customerName + "," + movieName + "," +
                seatNumber + "," + showTime + "," + price;
    }

    public static Booking fromFileString(String line) {
        String[] data = line.split(",");

        if (data.length != 6) {
            return null;
        }

        return new Booking(
                data[0],
                data[1],
                data[2],
                data[3],
                data[4],
                Double.parseDouble(data[5])
        );
    }
}


