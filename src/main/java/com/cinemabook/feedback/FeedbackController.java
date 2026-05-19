package com.cinemabook.feedback;

import jakarta.servlet.http.HttpSession;
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
                               HttpSession session,
                               Model model) {

        // Redirect to login if not logged in
        if (session.getAttribute("userId") == null) return "redirect:/login";

        List<Feedback> list = (filter != null && !filter.isBlank())
                ? feedbackService.filterBySentiment(filter)
                : feedbackService.getAll();

        model.addAttribute("feedbacks",     list);
        model.addAttribute("filter",        filter);
        model.addAttribute("avgRating",     feedbackService.getAverageRating());
        model.addAttribute("positiveCount", feedbackService.countBySentiment("POSITIVE"));
        model.addAttribute("negativeCount", feedbackService.countBySentiment("NEGATIVE"));
        model.addAttribute("totalCount",    feedbackService.getAll().size());

        // Pass session info to template
        model.addAttribute("sessionUserId",   session.getAttribute("userId"));
        model.addAttribute("sessionUserName", session.getAttribute("userName"));
        model.addAttribute("userRole",        session.getAttribute("userRole"));

        return "feedback/feedback";
    }

    // ─── CREATE ──────────────────────────────────────────────────
    @PostMapping("/add")
    public String addFeedback(@RequestParam String movieTitle,
                              @RequestParam int    rating,
                              @RequestParam String comment,
                              HttpSession session) {

        if (session.getAttribute("userId") == null) return "redirect:/login";

        String userId   = (String) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");

        feedbackService.addFeedback(userName, userId, movieTitle,
                rating, comment, LocalDate.now().toString());
        return "redirect:/feedback";
    }

    // ─── UPDATE (like) ───────────────────────────────────────────
    @PostMapping("/like/{id}")
    public String likeFeedback(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        feedbackService.likeFeedback(id);
        return "redirect:/feedback";
    }

    // ─── DELETE (permission-checked) ─────────────────────────────
    @PostMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";

        String userId   = (String) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");

        String result = feedbackService.deleteFeedback(id, userId, userRole);

        if ("forbidden".equals(result)) {
            return "redirect:/feedback?error=forbidden";
        }
        return "redirect:/feedback";
    }
}
