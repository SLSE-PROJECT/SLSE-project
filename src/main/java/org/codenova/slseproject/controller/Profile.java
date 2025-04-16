package org.codenova.slseproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.codenova.slseproject.entity.Champion;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.entity.UserChampion;
import org.codenova.slseproject.repository.ChampionRepository;
import org.codenova.slseproject.repository.UserChampionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.*;

@Controller
@AllArgsConstructor
public class Profile {

    private UserChampionRepository userChampionRepository;

    @GetMapping("/profile")
    public String profile(@SessionAttribute("user") Optional<User> userOpt, Model model) {

        if (userOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        User user = userOpt.get();

        List<UserChampion> ownedChampions = userChampionRepository.findByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("champions", ownedChampions);

        return "profile";
    }
}
