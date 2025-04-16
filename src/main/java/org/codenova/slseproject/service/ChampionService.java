package org.codenova.slseproject.service;

import lombok.AllArgsConstructor;
import org.codenova.slseproject.entity.Champion;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.entity.UserChampion;
import org.codenova.slseproject.repository.ChampionRepository;
import org.codenova.slseproject.repository.UserChampionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ChampionService {

    private final UserChampionRepository userChampionRepository;
    private final ChampionRepository championRepository;

    public List<UserChampion> getOrCreateUserChampions(User user) {
        int userId = user.getId();
        List<UserChampion> ownedChampions = userChampionRepository.findByUserId(userId);

        if (ownedChampions == null || ownedChampions.isEmpty()) {
            List<Champion> allChampions = championRepository.findAll();
            Collections.shuffle(allChampions);
            List<Champion> randomFive = allChampions.subList(0, 5);

            List<UserChampion> toSave = new ArrayList<>();

            for (Champion c : randomFive) {
                UserChampion uc = UserChampion.builder()
                        .userId(userId)
                        .championId(c.getId())
                        .name(c.getName())
                        .title(c.getTitle())
                        .imageUrl(c.getImageUrl())
                        .blurb(c.getBlurb())
                        .build();

                toSave.add(uc);
            }

            userChampionRepository.saveAll(userId, toSave);
            return toSave;
        }

        return ownedChampions;
    }
}
