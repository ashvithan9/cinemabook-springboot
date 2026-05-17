package com.cinemabook.user;

import com.cinemabook.movie.FeaturedPosterService;
import com.cinemabook.movie.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final MovieService movieService;
    private final FeaturedPosterService featuredPosterService;

    public UserController(UserService userService,
                          MovieService movieService,
                          FeaturedPosterService featuredPosterService) {
        this.userService = userService;
        this.movieService = movieService;
        this.featuredPosterService = featuredPosterService;
    }

    // ─── Auth ────────────────────────────────────────────

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("featuredMovies", featuredPosterService.getFeaturedMovies());
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session, Model model) {
        var user = userService.findByEmailAndPassword(email, password);
        if (user.isPresent()) {
            session.setAttribute("userId",   user.get().getUserId());
            session.setAttribute("userName", user.get().getName());
            session.setAttribute("userRole", user.get().getRole());
            return "redirect:/";
        }
        model.addAttribute("error", "Invalid email or password");
        model.addAttribute("featuredMovies", featuredPosterService.getFeaturedMovies());
        return "user/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("error", "Email already registered");
            return "user/register";
        }
        userService.saveUser(user);
        return "redirect:/login?registered";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ─── Admin: Featured Posters ──────────────────────────

    @GetMapping("/admin/featured-posters")
    public String featuredPostersPage(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("allMovies", movieService.getAllMovies());
        model.addAttribute("featuredIds", featuredPosterService.getFeaturedIds());
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "admin/featured-posters";
    }

    @PostMapping("/admin/featured-posters")
    public String saveFeaturedPosters(@RequestParam(value = "movieIds", required = false)
                                      List<String> movieIds,
                                      HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        featuredPosterService.saveFeaturedIds(movieIds != null ? movieIds : List.of());
        return "redirect:/admin/featured-posters?saved";
    }

    // ─── Admin: manage all users ─────────────────────────

    @GetMapping("/users")
    public String listUsers(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "user/list";
    }

    @GetMapping("/users/new")
    public String newUserForm(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("user", new User());
        model.addAttribute("isEdit", false);
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "user/form";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute User user, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("error", "Email already registered");
            model.addAttribute("user", user);
            model.addAttribute("isEdit", false);
            return "user/form";
        }
        userService.createUser(user);
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/edit")
    public String editForm(@PathVariable String id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        userService.getUserById(id).ifPresent(u -> model.addAttribute("user", u));
        model.addAttribute("isEdit", true);
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "user/form";
    }

    @PostMapping("/users/{id}/edit")
    public String update(@PathVariable String id, @ModelAttribute User user, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        user.setUserId(id);
        userService.updateUser(user);
        return "redirect:/users";
    }

    @PostMapping("/users/{id}/delete")
    public String delete(@PathVariable String id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        userService.deleteUser(id);
        return "redirect:/users";
    }

    // ─── User: own profile ───────────────────────────────

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        String userId = (String) session.getAttribute("userId");
        userService.getUserById(userId).ifPresent(u -> model.addAttribute("user", u));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "user/profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute User user, HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        user.setUserId((String) session.getAttribute("userId"));
        user.setRole("CUSTOMER");
        userService.updateUser(user);
        session.setAttribute("userName", user.getName());
        return "redirect:/profile?updated";
    }

    // ─── Helper ──────────────────────────────────────────

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }
}

