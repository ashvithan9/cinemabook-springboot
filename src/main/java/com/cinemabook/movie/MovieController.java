package com.cinemabook.movie;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // ─── Public ──────────────────────────────────────────

    @GetMapping
    public String list(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/login";
        model.addAttribute("nowShowing", movieService.getMoviesByStatus("NOW_SHOWING"));
        model.addAttribute("comingSoon", movieService.getMoviesByStatus("COMING_SOON"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "movie/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable String id, HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/login";
        movieService.getMovieById(id).ifPresent(m -> model.addAttribute("movie", m));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "movie/detail";
    }

    // ─── Admin only ───────────────────────────────────────

    @GetMapping("/new")
    public String showCreateForm(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("movie", new Movie());
        model.addAttribute("isEdit", false);
        return "movie/form";
    }

    @PostMapping
    public String create(@ModelAttribute Movie movie, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        movieService.saveMovie(movie);
        return "redirect:/movies";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        movieService.getMovieById(id).ifPresent(m -> model.addAttribute("movie", m));
        model.addAttribute("isEdit", true);
        return "movie/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable String id, @ModelAttribute Movie movie, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        movie.setMovieId(id);
        movieService.updateMovie(movie);
        return "redirect:/movies";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        movieService.deleteMovie(id);
        return "redirect:/movies";
    }

    // ─── Helpers ─────────────────────────────────────────

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }
}
