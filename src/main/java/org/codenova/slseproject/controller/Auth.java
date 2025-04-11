package org.codenova.slseproject.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.repository.UserRepository;
import org.codenova.slseproject.request.EmailCheck;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class Auth {

    private UserRepository userRepository;

    @GetMapping("/signup")
    public String signup() {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signupPost(@ModelAttribute User user, @ModelAttribute @Valid EmailCheck email, BindingResult result) {
        if (result.hasErrors()) {
            return "auth/signup";
        }
        user.setEmail(email.getEmail());
        userRepository.create(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public void login() {
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam("password") String password,
                            @ModelAttribute @Valid EmailCheck email, BindingResult result,
                            Model m, HttpSession session) {

        if (result.hasErrors()) {
            return "auth/login";
        }

        User user = userRepository.selectByEmail(email.getEmail());
        if (user == null) {
            return "auth/login";
        } else if (!user.getPassword().equals(password)) {
            return "auth/login";
        }

        session.setAttribute("user", user);

        return "redirect:/";
    }
}
