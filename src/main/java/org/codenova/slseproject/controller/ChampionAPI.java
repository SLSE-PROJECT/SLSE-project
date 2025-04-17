package org.codenova.slseproject.controller;

import lombok.AllArgsConstructor;
import org.codenova.slseproject.entity.ChampionPost;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.service.ChampionAPIService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/champion")
@AllArgsConstructor
public class ChampionAPI {

    private final ChampionAPIService championAPIService;

    // 챔피언 구매
    @GetMapping("/buy")
    public String buy(@SessionAttribute("user") Optional<User> user, @RequestParam("champion-id") String championId) {
        return championAPIService.buy(user, championId);
    }

    // 챔피언 댓글 목록 가져오기
    @GetMapping("/post")
    public List<ChampionPost> post(@RequestParam("champion-id") String championId) {
        return championAPIService.post(championId);
    }

    // 댓글 작성
    @PostMapping("/post")
    public String insertPost(@SessionAttribute("user") Optional<User> user,
                             @ModelAttribute ChampionPost championPost) {
        return championAPIService.insertPost(user, championPost);
    }

    // 댓글 삭제 - POST 방식으로 변경 (RESTful)
    @PostMapping("/delete-post")
    public String deletePost(@SessionAttribute("user") Optional<User> user,
                             @RequestParam("champion-post-id") Integer championPostId) {
        try {
            championAPIService.deletePost(user, championPostId);
            return "OK";
        } catch (Exception e) {
            return "FAIL";
        }
    }

    // 댓글 수정
    @PostMapping("/update-post")
    public String updatePost(@SessionAttribute("user") Optional<User> user,
                             @RequestParam("content") String content,
                             @RequestParam("champion-post-id") Integer postId) {
        try {
            championAPIService.updatePost(user, content, postId);
            return "OK";
        } catch (Exception e) {
            return "FAIL";
        }
    }

    // 댓글 조회 (프론트에서 필요한 정보만 추려서 전달)
    @GetMapping("/comments")
    public List<Map<String, Object>> getComments(@RequestParam("champion-id") String championId,
                                                 @SessionAttribute(value = "user", required = false) Optional<User> userOpt) {
        List<ChampionPost> posts = championAPIService.post(championId);

        return posts.stream().map(post -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", post.getId());
            map.put("nickname", post.getNickname());
            map.put("content", post.getContent());
            map.put("createdAt", post.getCreatedAt());
            map.put("isMine", userOpt.isPresent() && userOpt.get().getId() == post.getUserId());
            return map;
        }).collect(Collectors.toList());
    }
}
