package com.cinemabook.payment;

/**
 * Abstract base class for all payment methods.
 * Demonstrates: Abstraction, Encapsulation, Information Hiding
 */
public abstract class AbstractPayment {

    protected String paymentId;
    protected String userId;
    protected String bookingId;
    protected String amount;
    protected String status;
    protected String paymentDate;

    public AbstractPayment(String paymentId, String userId, String bookingId,
                           String amount, String status, String paymentDate) {
        this.paymentId   = paymentId;
        this.userId      = userId;
        this.bookingId   = bookingId;
        this.amount      = amount;
        this.status      = status;
        this.paymentDate = paymentDate;
    }

    // Abstraction — each payment type defines these
    public abstract String getPaymentMethod();
    public abstract String getPaymentType();
    public abstract boolean isRefundable();
    public abstract String getReceiptLabel();

    // Information Hiding — card data is masked internally
    protected String maskCardNumber(String fullNumber) {
        if (fullNumber == null || fullNumber.length() < 4) return "****";
        return "**** **** **** " + fullNumber.substring(fullNumber.length() - 4);
    }

    public String getFormattedAmount() { return "$" + amount; }

    // Getters
    public String getPaymentId()   { return paymentId; }
    public String getUserId()      { return userId; }
    public String getBookingId()   { return bookingId; }
    public String getAmount()      { return amount; }
    public String getStatus()      { return status; }
    public String getPaymentDate() { return paymentDate; }
}
