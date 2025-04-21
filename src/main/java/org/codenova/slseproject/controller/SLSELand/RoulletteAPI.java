package org.codenova.slseproject.controller.SLSELand;

import lombok.RequiredArgsConstructor;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.entity.vo.RouletteResult;
import org.codenova.slseproject.service.SLSELand.RouletteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/apply")
    public ResponseEntity<Map<String, Object>> applyForPoint(@SessionAttribute("user") User user) {
        Map<String, Object> response = new HashMap<>();
        int amount = rouletteService.applyForPoint(user.getId());
        if (amount > 0) {
            response.put("success", true);
            response.put("amount", amount);
        } else {
            response.put("success", false);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total")
    public ResponseEntity<Map<String, Object>> getTotalPoint() {
        int total = rouletteService.getTotalPoint();
        Map<String, Object> result = new HashMap<>();
        result.put("totalAmount", total);
        return ResponseEntity.ok(result);
    }
}
