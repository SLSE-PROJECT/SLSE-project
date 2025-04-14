package org.codenova.slseproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.codenova.slseproject.entity.Champion;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.service.ChampionAPIService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class Home {

    private ChampionAPIService championAPIService;

    public Home(ChampionAPIService championAPIService) {
        this.championAPIService = championAPIService;
    }

    @RequestMapping("/")
    public String home(@SessionAttribute("user")Optional<User> user, Model m) throws JsonProcessingException {

        Champion[] champions = championAPIService.findAllChampion();

        List<Champion> championList = Arrays.asList(champions);

        m.addAttribute("champions", championList);

        return "home";
    }
}
