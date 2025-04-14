package org.codenova.slseproject.controller;

import org.codenova.slseproject.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Optional;

@Controller
public class Home {

    @RequestMapping("/")
    public String home(@SessionAttribute("user")Optional<User> user){

        return "home";
    }
}
