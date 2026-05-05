package com.cinemabook.payment;

import org.springframework.stereotype.Service;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PaymentService {

    private static final String CSV_PATH = System.getProperty("user.dir") + "/data/payments.csv";
    private static final String HEADER = "paymentId,userId,bookingId,amount,status,paymentDate,cardHolderName,cardLastFour,cardType,paymentMethod";

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        File file = new File(CSV_PATH);
        if (!file.exists()) return payments;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] p = parseCSVLine(line);
                if (p.length == 10)
                    payments.add(new Payment(p[0],p[1],p[2],p[3],p[4],p[5],p[6],p[7],p[8],p[9]));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return payments;
    }

    public List<Payment> getPaymentsByUserId(String userId) {
        return getAllPayments().stream()
                .filter(p -> p.getUserId().equals(userId))
                .toList();
    }

    public Optional<Payment> getPaymentById(String paymentId) {
        return getAllPayments().stream()
                .filter(p -> p.getPaymentId().equals(paymentId))
                .findFirst();
    }

    public double getTotalRevenue() {
        return getAllPayments().stream()
                .filter(p -> p.getStatus().equals("COMPLETED"))
                .mapToDouble(p -> {
                    try { return Double.parseDouble(p.getAmount()); }
                    catch (NumberFormatException e) { return 0; }
                }).sum();
    }

    public long countByStatus(String status) {
        return getAllPayments().stream()
                .filter(p -> p.getStatus().equalsIgnoreCase(status))
                .count();
    }

    public void savePayment(Payment payment) {
        payment.setPaymentId("PAY" + System.currentTimeMillis());
        payment.setPaymentDate(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        payment.setStatus("COMPLETED");
        File file = new File(CSV_PATH);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            if (file.length() == 0) bw.write(HEADER + "\n");
            bw.write(toCsvLine(payment) + "\n");
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void updatePayment(Payment updated) {
        List<Payment> payments = getAllPayments();
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getPaymentId().equals(updated.getPaymentId())) {
                payments.set(i, updated);
                break;
            }
        }
        writeAll(payments);
    }

    public void deletePayment(String paymentId) {
        List<Payment> payments = getAllPayments();
        payments.removeIf(p -> p.getPaymentId().equals(paymentId));
        writeAll(payments);
    }

    private void writeAll(List<Payment> payments) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_PATH, false))) {
            bw.write(HEADER + "\n");
            for (Payment p : payments) bw.write(toCsvLine(p) + "\n");
        } catch (IOException e) { e.printStackTrace(); }
    }

    private String toCsvLine(Payment p) {
        return String.join(",",
                p.getPaymentId(), p.getUserId(), p.getBookingId(),
                p.getAmount(), p.getStatus(), p.getPaymentDate(),
                "\"" + p.getCardHolderName() + "\"",
                p.getCardLastFour(), p.getCardType(), p.getPaymentMethod());
    }

    private String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (c == '"') { inQuotes = !inQuotes; }
            else if (c == ',' && !inQuotes) {
                fields.add(current.toString().trim());
                current = new StringBuilder();
            } else { current.append(c); }
        }
        fields.add(current.toString().trim());
        return fields.toArray(new String[0]);
    }
}
