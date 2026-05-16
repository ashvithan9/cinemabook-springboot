package com.cinemabook.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private String paymentId;
    private String userId;
    private String bookingId;
    private String amount;
    private String status; // PENDING, COMPLETED, FAILED
    private String paymentDate;
    private String cardHolderName;
    private String cardLastFour;
    private String cardType; // VISA, MASTERCARD, AMEX
    private String paymentMethod; // CREDIT, DEBIT
}