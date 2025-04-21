package org.codenova.slseproject.controller.SLSELand;

import org.codenova.slseproject.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Optional;

@Controller
public class SLSELand {

    @GetMapping("/slseland")
    public String showPage(@SessionAttribute("user") Optional<User> user, Model m) {
        if(user.isEmpty()){
            return "redirect:/auth/login";
        }
        m.addAttribute("user", user.get());
        return "slseland";
    }
}