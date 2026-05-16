package com.cinemabook.payment;

/**
 * Credit card payment — refundable within 24 hours.
 * Demonstrates: Inheritance, Polymorphism
 */
public class CreditCardPayment extends AbstractPayment {

    private final String cardHolderName;
    private final String cardLastFour;
    private final String cardType;

    public CreditCardPayment(String paymentId, String userId, String bookingId,
                             String amount, String status, String paymentDate,
                             String cardHolderName, String cardLastFour, String cardType) {
        super(paymentId, userId, bookingId, amount, status, paymentDate);
        this.cardHolderName = cardHolderName;
        this.cardLastFour   = cardLastFour;
        this.cardType       = cardType;
    }

    @Override public String getPaymentMethod()  { return "CREDIT"; }
    @Override public String getPaymentType()    { return cardType + " Credit Card"; }
    @Override public boolean isRefundable()     { return true; }
    @Override public String getReceiptLabel()   {
        return cardType + " ending in " + cardLastFour;
    }

    public String getCardHolderName() { return cardHolderName; }
    public String getCardLastFour()   { return cardLastFour; }
    public String getCardType()       { return cardType; }
}
