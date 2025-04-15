package org.codenova.slseproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.codenova.slseproject.entity.Champion;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.service.ChampionAPIService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.*;

@Controller
public class Profile {

    private final ChampionAPIService championAPIService;

    public Profile(ChampionAPIService championAPIService) {
        this.championAPIService = championAPIService;
    }

    @GetMapping("/profile")
    public String profile(@SessionAttribute("user") Optional<User> userOpt, Model model) throws JsonProcessingException {

        if (userOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        User user = userOpt.get();

        // 처음 접속한 유저라면 챔피언 5개 랜덤 지급
        if (user.getChampions() == null || user.getChampions().isEmpty()) {
            Champion[] allChampions = championAPIService.findAllChampion();
            List<Champion> championList = Arrays.asList(allChampions);
            Collections.shuffle(championList);
            List<Champion> selectedChampions = new ArrayList<>(championList.subList(0, 5));
            user.setChampions(selectedChampions);  // 한 번만 저장
        }

        model.addAttribute("user", user);
        model.addAttribute("champions", user.getChampions());

        return "profile";
    }
}
