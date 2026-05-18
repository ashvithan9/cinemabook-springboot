package com.cinemabook.show;

import com.cinemabook.movie.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/shows")
public class ShowController {

    private final ShowService showService;
    private final MovieService movieService;

    public ShowController(ShowService showService, MovieService movieService) {
        this.showService = showService;
        this.movieService = movieService;
    }

    // List shows — filtered by movieId if provided (customer view)
    // or all shows (admin view)
    @GetMapping
    public String list(@RequestParam(required = false) String movieId,
                       HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/login";
        if (movieId != null && !movieId.isEmpty()) {
            model.addAttribute("shows", showService.getShowsByMovieId(movieId));
            movieService.getMovieById(movieId).ifPresent(m -> model.addAttribute("movie", m));
            model.addAttribute("movieId", movieId);
        } else {
            if (!isAdmin(session)) return "redirect:/movies";
            model.addAttribute("shows", showService.getAllShows());
        }
        model.addAttribute("userRole", session.getAttribute("userRole"));
        model.addAttribute("userName", session.getAttribute("userName"));
        return "show/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable String id, HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/login";
        showService.getShowById(id).ifPresent(s -> model.addAttribute("show", s));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "show/detail";
    }

    // Admin: add show form
    @GetMapping("/new")
    public String newForm(HttpSession session, Model model,
                          @RequestParam(required = false) String movieId) {
        if (!isAdmin(session)) return "redirect:/login";
        Show show = new Show();
        if (movieId != null) {
            show.setMovieId(movieId);
            movieService.getMovieById(movieId).ifPresent(m -> {
                show.setMovieTitle(m.getTitle());
                model.addAttribute("movie", m);
            });
        }
        model.addAttribute("show", show);
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("isEdit", false);
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "show/form";
    }

    @PostMapping
    public String create(@ModelAttribute Show show, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        // auto-fill movie title from movie
        if (show.getMovieTitle() == null || show.getMovieTitle().isEmpty()) {
            movieService.getMovieById(show.getMovieId())
                    .ifPresent(m -> show.setMovieTitle(m.getTitle()));
        }
        showService.saveShow(show);
        return "redirect:/shows?movieId=" + show.getMovieId();
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        showService.getShowById(id).ifPresent(s -> model.addAttribute("show", s));
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("isEdit", true);
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "show/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable String id, @ModelAttribute Show show, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        show.setShowId(id);
        showService.updateShow(show);
        return "redirect:/shows";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        showService.deleteShow(id);
        return "redirect:/shows";
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }
}
