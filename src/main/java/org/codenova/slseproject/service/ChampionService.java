package org.codenova.slseproject.service;

import lombok.AllArgsConstructor;
import org.codenova.slseproject.entity.Champion;
import org.codenova.slseproject.entity.ChampionPost;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.entity.UserChampion;
import org.codenova.slseproject.repository.ChampionPostRepository;
import org.codenova.slseproject.repository.ChampionRepository;
import org.codenova.slseproject.repository.UserChampionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChampionService {

    private final UserChampionRepository userChampionRepository;
    private final ChampionRepository championRepository;
    private final ChampionPostRepository championPostRepository;


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
                userChampionRepository.insert(uc);
            }
            return toSave;
        }
        return ownedChampions;
    }
    public List<ChampionPost> getCommentsByChampionId(String championId) {
        return championPostRepository.selectByChampionId(championId);
    }
    public String insertPost(Optional<User> user, ChampionPost championPost) {
        if (user.isEmpty()) return "NO_USER";

        championPost.setUserId(user.get().getId());
        championPost.setNickname(user.get().getNickname()); // 닉네임 설정
        championPost.setCreatedAt(LocalDateTime.now());
        championPostRepository.insert(championPost);
        return "OK";
    }
}
