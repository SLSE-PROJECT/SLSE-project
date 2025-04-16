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

    private final ChampionRepository championRepository;
    private final UserChampionRepository userChampionRepository;
    private final UserRepository userRepository;
    private final ChampionPostRepository championPostRepository;

    // 챔피언 구매 로직
    @Transactional
    public String buy(Optional<User> user, String championId) {
        if (user.isEmpty()) return "NNNN";

        Champion champion = championRepository.findByChampionId(championId);
        if (champion == null) return "NNNN";

        if (userChampionRepository.alreadyOwned(user.get().getId(), championId) != null) return "NNNN";

        if (user.get().getSLSE() < champion.getPrice()) return "NNNN";

        int updatedSlse = user.get().getSLSE() - champion.getPrice();
        userRepository.updateSLSE(updatedSlse, user.get().getId());

        UserChampion userChampion = UserChampion.builder()
                .userId(user.get().getId())
                .championId(champion.getId())
                .name(champion.getName())
                .title(champion.getTitle())
                .imageUrl(champion.getImageUrl())
                .blurb(champion.getBlurb())
                .build();
        userChampionRepository.insert(userChampion);

        return "YYYY";
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

}
