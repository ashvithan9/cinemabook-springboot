package com.cinemabook.show;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    @GetMapping
    public String getShows(Model model) {

        model.addAttribute("shows",
                showService.getAllShows());

        return "show/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {

        model.addAttribute("show", new Show());

        return "show/add";
    }

    @PostMapping("/add")
    public String addShow(@ModelAttribute Show show) {

        showService.addShow(show);

        return "redirect:/shows";
    }
}