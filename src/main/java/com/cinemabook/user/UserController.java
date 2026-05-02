package com.cinemabook.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ─── Auth ───────────────────────────────────────────

    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session, Model model) {
        var user = userService.findByEmailAndPassword(email, password);
        if (user.isPresent()) {
            session.setAttribute("userId", user.get().getUserId());
            session.setAttribute("userName", user.get().getName());
            session.setAttribute("userRole", user.get().getRole());
            return "redirect:/";
        }
        model.addAttribute("error", "Invalid email or password");
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

    // ─── Admin: manage all users ─────────────────────────

    @GetMapping("/users")
    public String listUsers(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("users", userService.getAllUsers());
        return "user/list";
    }

    @GetMapping("/users/{id}/edit")
    public String editForm(@PathVariable String id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        userService.getUserById(id).ifPresent(u -> model.addAttribute("user", u));
        model.addAttribute("isEdit", true);
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
        return "user/profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute User user, HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        user.setUserId((String) session.getAttribute("userId"));
        user.setRole("CUSTOMER");
        userService.updateUser(user);
        session.setAttribute("userName", user.getName());
        return "redirect:/profile";
    }

    // ─── Helper ──────────────────────────────────────────

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }
}