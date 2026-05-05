package com.cinemabook.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // ─── READ — show all or filtered ─────────────────────────────
    @GetMapping
    public String showFeedback(@RequestParam(required = false) String filter,
                               Model model) {

        List<Feedback> list = (filter != null && !filter.isBlank())
                ? feedbackService.filterBySentiment(filter)
                : feedbackService.getAll();

        model.addAttribute("feedbacks",     list);
        model.addAttribute("filter",        filter);
        model.addAttribute("avgRating",     feedbackService.getAverageRating());
        model.addAttribute("positiveCount", feedbackService.countBySentiment("POSITIVE"));
        model.addAttribute("negativeCount", feedbackService.countBySentiment("NEGATIVE"));
        model.addAttribute("totalCount",    feedbackService.getAll().size());

        return "feedback/feedback"; // → templates/feedback/feedback.html
    }

    // ─── CREATE ──────────────────────────────────────────────────
    @PostMapping("/add")
    public String addFeedback(@RequestParam String username,
                              @RequestParam String movieTitle,
                              @RequestParam int    rating,
                              @RequestParam String comment) {
        feedbackService.addFeedback(username, movieTitle, rating,
                comment, LocalDate.now().toString());
        return "redirect:/feedback";
    }

    // ─── UPDATE (like) ───────────────────────────────────────────
    @PostMapping("/like/{id}")
    public String likeFeedback(@PathVariable int id) {
        feedbackService.likeFeedback(id);
        return "redirect:/feedback";
    }

    // ─── DELETE ──────────────────────────────────────────────────
    @PostMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable int id) {
        feedbackService.deleteFeedback(id);
        return "redirect:/feedback";
    }
}
