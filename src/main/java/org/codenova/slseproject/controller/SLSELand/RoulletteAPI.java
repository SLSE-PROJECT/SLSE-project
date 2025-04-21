package org.codenova.slseproject.controller.SLSELand;

import lombok.RequiredArgsConstructor;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.entity.vo.RouletteResult;
import org.codenova.slseproject.service.SLSELand.RouletteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/roulette")
@RequiredArgsConstructor
public class RoulletteAPI {

    private final RouletteService rouletteService;

    @PostMapping("/spin")
    public ResponseEntity<RouletteResult> spinRoulette(@SessionAttribute(value = "user", required = false) User user) {
        if (user == null) {
            System.out.println("유저 못찾음");
        }

        RouletteResult result = rouletteService.spin(user);
        return ResponseEntity.ok(result);
    }
}
