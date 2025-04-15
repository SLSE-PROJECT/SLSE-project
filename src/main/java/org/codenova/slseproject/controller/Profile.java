package org.codenova.slseproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.codenova.slseproject.entity.Champion;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.entity.UserChampion;
import org.codenova.slseproject.repository.UserChampionRepository;
import org.codenova.slseproject.service.ChampionAPIService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.*;

@Controller
public class Profile {

    private final ChampionAPIService championAPIService;
    private final UserChampionRepository userChampionRepository;

    public Profile(ChampionAPIService championAPIService, UserChampionRepository userChampionRepository) {
        this.championAPIService = championAPIService;
        this.userChampionRepository = userChampionRepository;
    }

    @GetMapping("/profile")
    public String profile(@SessionAttribute("user") Optional<User> userOpt, Model model) throws JsonProcessingException {

        if (userOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        User user = userOpt.get();
        int userId = user.getId();

        List<UserChampion> ownedChampions = userChampionRepository.findByUserId(userId);

        if (ownedChampions == null || ownedChampions.isEmpty()) {
            Champion[] allChampions = championAPIService.findAllChampion();
            List<Champion> championList = Arrays.asList(allChampions);
            Collections.shuffle(championList);
            List<Champion> randomFive = championList.subList(0, 5);

            List<UserChampion> toSave = new ArrayList<>();
            for (Champion c : randomFive) {
                UserChampion uc = new UserChampion();
                uc.setUserId(userId);
                uc.setChampionId(c.getId());
                uc.setName(c.getName());
                uc.setTitle(c.getTitle());
                uc.setImageUrl(c.getImageUrl());
                uc.setBlurb(c.getBlurb());
                toSave.add(uc);
            }

            userChampionRepository.saveAll(userId, toSave);
            ownedChampions = toSave;
        } else {

            Champion[] allChampions = championAPIService.findAllChampion();
            Map<String, String> blurbMap = new HashMap<>();
            for (Champion c : allChampions) {
                blurbMap.put(c.getId(), c.getBlurb());
            }

            for (UserChampion uc : ownedChampions) {
                String blurb = blurbMap.get(uc.getChampionId());
                uc.setBlurb(blurb);
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("champions", ownedChampions);

        return "profile";
    }
}
