package org.codenova.slseproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.codenova.slseproject.service.ChampionAPIService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/champion")
public class Champion {

    private ChampionAPIService championAPIService;

    public Champion(ChampionAPIService championAPIService) {
        this.championAPIService = championAPIService;
    }

    @GetMapping("/list")
    public String list(Model model) throws JsonProcessingException {

        org.codenova.slseproject.entity.Champion[] champions = championAPIService.findAllChampion();

        List<org.codenova.slseproject.entity.Champion> championList = Arrays.asList(champions);

        model.addAttribute("champions", championList);

        return "champion/list";
    }
}