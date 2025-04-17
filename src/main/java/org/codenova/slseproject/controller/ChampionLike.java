package org.codenova.slseproject.controller;

import jakarta.servlet.http.HttpSession;
import org.codenova.slseproject.repository.ChampionLikeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.codenova.slseproject.entity.User;

import java.util.List;

@RestController
@RequestMapping("/api/like")
public class ChampionLike {

    private final ChampionLikeRepository likeRepository;

    public ChampionLike(ChampionLikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @PostMapping("/{championId}")
    public ResponseEntity<String> toggleLike(@PathVariable String championId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");

        boolean liked = likeRepository.existsLike(user.getId(), championId);
        if (liked) {
            likeRepository.deleteLike(user.getId(), championId);
            return ResponseEntity.ok("unliked");
        } else {
            likeRepository.insertLike(user.getId(), championId);
            return ResponseEntity.ok("liked");
        }
    }
    @GetMapping("/my")
    public ResponseEntity<List<String>> getLikedChampionIds(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        List<String> likedChampionIds = likeRepository.selectLikedChampionIds(user.getId());
        return ResponseEntity.ok(likedChampionIds);
    }
}