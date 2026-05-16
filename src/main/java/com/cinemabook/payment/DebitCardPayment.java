package com.cinemabook.payment;

/**
 * Debit card payment — non-refundable.
 * Demonstrates: Inheritance, Polymorphism
 */
public class DebitCardPayment extends AbstractPayment {

    private final String cardHolderName;
    private final String cardLastFour;
    private final String cardType;

    public DebitCardPayment(String paymentId, String userId, String bookingId,
                            String amount, String status, String paymentDate,
                            String cardHolderName, String cardLastFour, String cardType) {
        super(paymentId, userId, bookingId, amount, status, paymentDate);
        this.cardHolderName = cardHolderName;
        this.cardLastFour   = cardLastFour;
        this.cardType       = cardType;
    }

    @Override public String getPaymentMethod()  { return "DEBIT"; }
    @Override public String getPaymentType()    { return cardType + " Debit Card"; }
    @Override public boolean isRefundable()     { return false; }
    @Override public String getReceiptLabel()   {
        return "Debit " + cardType + " ending in " + cardLastFour;
    }

    public String getCardHolderName() { return cardHolderName; }
    public String getCardLastFour()   { return cardLastFour; }
    public String getCardType()       { return cardType; }
}
