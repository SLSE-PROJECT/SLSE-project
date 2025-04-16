package org.codenova.slseproject.controller;

import lombok.AllArgsConstructor;
import org.codenova.slseproject.entity.ChampionPost;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.service.ChampionAPIService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public List<ChampionPost> post (@RequestParam("champion-id") String championId){
        return championAPIService.post(championId);
    }

    @GetMapping("/insert-post")
    public String insertPost(@SessionAttribute("user") Optional<User> user, @ModelAttribute ChampionPost championPost){
        return championAPIService.insertPost(user, championPost);
    }

    @GetMapping("/delete-post")
    public void deletePost (@SessionAttribute("user") Optional<User> user, @RequestParam("champion-post-id") Integer championPostId){
        championAPIService.deletePost(user, championPostId);
    }

    @GetMapping("/update-post")
    public void updatPost (@SessionAttribute("user") Optional<User> user, @RequestParam("content") String content, @RequestParam("champion-post-id") Integer championPostId){
        championAPIService.updatePost(user, content, championPostId);
    }

}
