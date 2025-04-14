package org.codenova.slseproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.codenova.slseproject.entity.Champion;
import org.codenova.slseproject.service.ChampionAPIService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/champion")
public class ChampionController {

    private ChampionAPIService championAPIService;

    public ChampionController(ChampionAPIService championAPIService) {
        this.championAPIService = championAPIService;
    }

    @GetMapping("/list")
    public String list(Model model) throws JsonProcessingException {

        Champion[] champions = championAPIService.findAllChampion();

        List<Champion> championList = Arrays.asList(champions);

        model.addAttribute("champions", championList);

        return "champion/list";
    }
}