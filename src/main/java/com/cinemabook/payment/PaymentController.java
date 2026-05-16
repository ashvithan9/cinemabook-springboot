package com.cinemabook.payment;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ─── User: own payments ───────────────────────────────

    @GetMapping("/my")
    public String myPayments(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/login";
        String userId = (String) session.getAttribute("userId");
        model.addAttribute("payments", paymentService.getPaymentsByUserId(userId));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        model.addAttribute("userName", session.getAttribute("userName"));
        return "payment/list";
    }

    @GetMapping("/new")
    public String showPaymentForm(HttpSession session, Model model,
                                  @RequestParam(required = false) String bookingId,
                                  @RequestParam(required = false) String amount) {
        if (!isLoggedIn(session)) return "redirect:/login";
        Payment payment = new Payment();
        payment.setBookingId(bookingId != null ? bookingId : "");
        payment.setAmount(amount != null ? amount : "");
        model.addAttribute("payment", payment);
        model.addAttribute("userRole", session.getAttribute("userRole"));
        model.addAttribute("userName", session.getAttribute("userName"));
        return "payment/form";
    }

    @PostMapping
    public String processPayment(@ModelAttribute Payment payment, HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/login";
        payment.setUserId((String) session.getAttribute("userId"));
        // mask card number — only save last 4 digits
        String card = payment.getCardLastFour();
        if (card != null && card.length() > 4)
            payment.setCardLastFour(card.substring(card.length() - 4));
        paymentService.savePayment(payment);
        return "redirect:/payments/my?success";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable String id, HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/login";
        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("userRole");
        paymentService.getPaymentById(id).ifPresent(p -> {
            // only owner or admin can view
            if (p.getUserId().equals(userId) || "ADMIN".equals(role))
                model.addAttribute("payment", p);
        });
        model.addAttribute("userRole", role);
        model.addAttribute("userName", session.getAttribute("userName"));
        return "payment/detail";
    }

    // ─── Admin only ───────────────────────────────────────

    @GetMapping
    public String allPayments(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        List<Payment> all = paymentService.getAllPayments();
        model.addAttribute("payments", all);
        model.addAttribute("totalRevenue", paymentService.getTotalRevenue());
        model.addAttribute("completedCount", paymentService.countByStatus("COMPLETED"));
        model.addAttribute("pendingCount", paymentService.countByStatus("PENDING"));
        model.addAttribute("failedCount", paymentService.countByStatus("FAILED"));
        model.addAttribute("totalCount", all.size());
        model.addAttribute("userRole", session.getAttribute("userRole"));
        model.addAttribute("userName", session.getAttribute("userName"));
        return "payment/list";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        paymentService.getPaymentById(id).ifPresent(p -> model.addAttribute("payment", p));
        model.addAttribute("isEdit", true);
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "payment/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable String id, @ModelAttribute Payment payment, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        payment.setPaymentId(id);
        paymentService.updatePayment(payment);
        return "redirect:/payments";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        paymentService.deletePayment(id);
        return "redirect:/payments";
    }

    // ─── Helpers ─────────────────────────────────────────

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }
}
