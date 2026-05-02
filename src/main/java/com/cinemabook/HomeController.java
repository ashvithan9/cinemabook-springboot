package com.cinemabook;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "index";
    }
}