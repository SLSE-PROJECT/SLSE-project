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
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChampionAPIService {

    private ChampionRepository championRepository;
    private UserChampionRepository userChampionRepository;
    private UserRepository userRepository;
    private ChampionPostRepository championPostRepository;

    public String buy(Optional<User> user, String championId){

        System.out.println(championRepository.findByChampionId(championId));

        if(user.isEmpty()){
            return "failed";
        }

        Champion champion = championRepository.findByChampionId(championId);
        if(champion == null){
            return "failed";
        }

        if(userChampionRepository.alreadyOwned(user.get().getId(), championId) != null){
            return "failed";
        }

        if(user.get().getSLSE() < champion.getPrice()){
            return "failed";
        }

        int currentSLSE = userRepository.selectByEmail(user.get().getEmail()).getSLSE();
        Integer SLSE = currentSLSE - champion.getPrice();
        userRepository.updateSLSE(SLSE, user.get().getId());

        UserChampion userChampion = UserChampion.builder()
                .userId(user.get().getId())
                .championId(champion.getId())
                .name(champion.getName())
                .title(champion.getTitle())
                .imageUrl(champion.getImageUrl())
                .blurb(champion.getBlurb())
                .build();
        try {
            userChampionRepository.insert(userChampion);
            System.out.println("인서트 성공");
        } catch (Exception e) {
            System.out.println(e);
        }

        return "success";
    }

    public List<ChampionPost> post(String championId){
        return championPostRepository.selectByChampionId(championId);
    }

    public String insertPost(Optional<User> user, ChampionPost championPost){

        if(user.isEmpty()){
            return "NNN";
        }

        championPost.setUserId(user.get().getId());
        championPost.setNickname(user.get().getNickname()); // ✅ 닉네임 세팅
        championPost.setCreatedAt(LocalDateTime.now());     // ✅ 등록일 세팅
        championPostRepository.insert(championPost);

        return "OK"; // ✅ 클라이언트와 통일
    }

    public void deletePost(Optional<User> user, Integer championPostId){
        if (user.isEmpty()) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        ChampionPost championPost = championPostRepository.selectById(championPostId);

        boolean isOwner = user.get().getId() == championPost.getUserId();

        if (!isOwner) {
            throw new SecurityException("본인만 게시글을 삭제할 수 있습니다.");
        }

        championPostRepository.deletePost(championPost.getId());
    }

    public void updatePost(Optional<User> user, String content, Integer championPostId){
        if (user.isEmpty()) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        ChampionPost championPost = championPostRepository.selectById(championPostId);

        boolean isOwner = user.get().getId() == championPost.getUserId(); // ✅ 수정

        if (!isOwner) {
            throw new SecurityException("본인만 게시글을 수정할 수 있습니다.");
        }

        championPostRepository.updatePost(content, championPostId);
    }
}
