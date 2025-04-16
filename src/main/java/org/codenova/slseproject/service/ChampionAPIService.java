package org.codenova.slseproject.service;

import lombok.AllArgsConstructor;
import org.codenova.slseproject.entity.Champion;
import org.codenova.slseproject.entity.ChampionPost;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.entity.UserChampion;
import org.codenova.slseproject.repository.ChampionPostRepository;
import org.codenova.slseproject.repository.ChampionRepository;
import org.codenova.slseproject.repository.UserChampionRepository;
import org.codenova.slseproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ChampionAPIService {

    private ChampionRepository championRepository;
    private UserChampionRepository userChampionRepository;
    private UserRepository userRepository;
    private ChampionPostRepository championPostRepository;

    @Transactional
    public String buy(Optional<User> user, String championId){

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

        Integer SLSE = user.get().getSLSE() - champion.getPrice();
        userRepository.updateSLSE(SLSE, user.get().getId());

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

    public String post(Optional<User> user, ChampionPost championPost){

        if(user.isEmpty()){
            return "NNN";
        }

        championPost.setUserId(user.get().getId());
        championPostRepository.insert(championPost);

        return "YYY";
    }
}
