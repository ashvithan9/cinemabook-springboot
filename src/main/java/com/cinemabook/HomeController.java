package com.cinemabook;

import com.cinemabook.movie.MovieService;
import com.cinemabook.movie.FeaturedPosterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final MovieService movieService;
    private final FeaturedPosterService featuredPosterService;

    public HomeController(MovieService movieService,
                          FeaturedPosterService featuredPosterService) {
        this.movieService = movieService;
        this.featuredPosterService = featuredPosterService;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));

        // If admin has selected featured movies use those, otherwise all NOW_SHOWING
        List<?> featured = featuredPosterService.getFeaturedIds().isEmpty()
                ? movieService.getMoviesByStatus("NOW_SHOWING")
                : featuredPosterService.getFeaturedMovies();

        model.addAttribute("nowShowingMovies", featured);
        return "index";
    }
}