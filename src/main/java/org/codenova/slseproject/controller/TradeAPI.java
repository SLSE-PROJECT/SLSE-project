package org.codenova.slseproject.controller;

import lombok.AllArgsConstructor;
import org.codenova.slseproject.entity.Champion;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.entity.UserChampion;
import org.codenova.slseproject.repository.ChampionRepository;
import org.codenova.slseproject.repository.UserChampionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/trade")
@AllArgsConstructor
public class TradeAPI {

    ChampionRepository championRepository;
    UserChampionRepository userChampionRepository;

    @GetMapping("/buy")
    public String buy(@SessionAttribute("user") Optional<User> user, @RequestParam("champion-id") String championId){

        if(user.isEmpty()){
            return "NNNN";
        }

        Champion champion = championRepository.findByChampionId(championId);
        if(champion == null){
            return "NNNN";
        }

        if(userChampionRepository.alreadyOwned(user.get().getId(), championId) != null){
            return "NNNN";
        }

        if(user.get().getSLSE() < champion.getPrice()){

            return "NNNN";
        }

        UserChampion userChampion = UserChampion.builder()
                .userId(user.get().getId())
                .name(champion.getName())
                .title(champion.getTitle())
                .imageUrl(champion.getImageUrl())
                .blurb(champion.getBlurb())
                .build();
        userChampionRepository.insert(userChampion);

        return "YYYY";
    }
}
