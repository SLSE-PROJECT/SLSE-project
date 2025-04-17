package org.codenova.slseproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Page {

    @GetMapping("/SLSELAND")
    public String casinoPage() {
        return "SLSELAND";
    }

    @GetMapping("/nicknamemarket")
    public String nicknamePage() {
        return "nicknamemarket";
    }
}