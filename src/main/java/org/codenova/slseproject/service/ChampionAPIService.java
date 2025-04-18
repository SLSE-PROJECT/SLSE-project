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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChampionAPIService {

    private ChampionRepository championRepository;
    private UserChampionRepository userChampionRepository;
    private UserRepository userRepository;
    private ChampionPostRepository championPostRepository;


    public String buy(Optional<User> user, String championId) {
        if (user.isEmpty()) {
            return "failed";
        }

        Champion champion = championRepository.findByChampionId(championId);
        if (champion == null) {
            return "failed";
        }

        int userId = user.get().getId();
        int userSLSE = userRepository.selectById(userId).getSLSE();
        int price = champion.getPrice();

        if (userSLSE < price) {
            return "failed";
        }

        // SLSE 차감
        userRepository.updateSLSE(userSLSE - price, userId);

        // 중복 여부 확인
        UserChampion owned = userChampionRepository.alreadyOwned(userId, championId);

        if (owned != null) {
            owned.setQuantity(owned.getQuantity() + 1);
            applyUpgradeLogic(owned); // 등급 계산 + 수량 리셋
            userChampionRepository.update(owned);
        } else {
            UserChampion newChamp = UserChampion.builder()
                    .userId(userId)
                    .championId(champion.getId())
                    .name(champion.getName())
                    .title(champion.getTitle())
                    .imageUrl(champion.getImageUrl())
                    .blurb(champion.getBlurb())
                    .quantity(1)
                    .grade(1)
                    .build();

            userChampionRepository.insert(newChamp);
        }

        return "success";
    }

    private void applyUpgradeLogic(UserChampion champ) {
        int quantity = champ.getQuantity();
        int grade = champ.getGrade();

        Map<Integer, Integer> requiredMap = Map.of(
                1, 2,
                2, 4,
                3, 10,
                4, 20
        );

        while (grade < 5 && quantity >= requiredMap.getOrDefault(grade, Integer.MAX_VALUE)) {
            quantity = 0;  // 진화 시 수량 초기화
            grade++;
        }

        champ.setQuantity(quantity);
        champ.setGrade(grade);
    }

    // 댓글 리스트 반환
    public List<ChampionPost> post(String championId) {
        return championPostRepository.selectByChampionId(championId);
    }

    // 댓글 등록
    public String insertPost(Optional<User> user, ChampionPost championPost) {
        if (user.isEmpty()) return "NNN";

        championPost.setUserId(user.get().getId());
        championPost.setNickname(user.get().getNickname());     // 닉네임 추가
        championPost.setCreatedAt(LocalDateTime.now());         // 등록 시간 추가
        championPost.setDeleted(false);                         // 기본 삭제 상태 false
        championPostRepository.insert(championPost);

        return "OK";
    }

    // 댓글 삭제
    public void deletePost(Optional<User> user, Integer championPostId) {
        if (user.isEmpty()) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        ChampionPost post = championPostRepository.selectById(championPostId);
        if (post == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }

        if (user.get().getId() != post.getUserId()) {
            throw new SecurityException("본인만 댓글을 삭제할 수 있습니다.");
        }

        championPostRepository.deletePost(championPostId);
    }

    // 댓글 수정
    public void updatePost(Optional<User> user, String content, Integer championPostId) {
        if (user.isEmpty()) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        ChampionPost post = championPostRepository.selectById(championPostId);
        if (post == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }

        if (user.get().getId() != post.getUserId()) {
            throw new SecurityException("본인만 댓글을 수정할 수 있습니다.");
        }

        post.setContent(content);
        post.setUpdatedAt(LocalDateTime.now()); // ✅ 수정 시간 기록
        championPostRepository.updatePost(content, championPostId);
    }
    // 검색기능
    public List<Map<String, Object>> searchByName(String keyword) {
        List<Champion> result = championRepository.searchByName(keyword + "%");

        return result.stream().map(champ -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", champ.getId());
            map.put("name", champ.getName());
            map.put("title", champ.getTitle());
            map.put("blurb", champ.getBlurb());
            map.put("imageUrl", champ.getImageUrl());
            map.put("price", champ.getPrice());
            return map;
        }).collect(Collectors.toList());
    }

}
