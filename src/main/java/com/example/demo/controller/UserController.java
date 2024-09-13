package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String getSignup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String password) {
        userService.saveUser(username, password);
        return "redirect:/login?success=true";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("errorMessage", "Invalid username or password!");
        return "login";
    }
}
