package org.codenova.slseproject.controller;

import lombok.AllArgsConstructor;
import org.codenova.slseproject.entity.ChampionPost;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.service.ChampionAPIService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/champion")
@AllArgsConstructor
public class ChampionAPI {

    ChampionAPIService championAPIService;

    @GetMapping("/buy")
    public String buy(@SessionAttribute("user") Optional<User> user, @RequestParam("champion-id") String championId){
        return championAPIService.buy(user, championId);
    }

    @GetMapping("/post")
    public String post(@SessionAttribute("user") Optional<User> user, @ModelAttribute ChampionPost championPost){
        return championAPIService.post(user, championPost);
    }
}
