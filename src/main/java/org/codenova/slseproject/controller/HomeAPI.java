package org.codenova.slseproject.controller;

import lombok.AllArgsConstructor;
import org.codenova.slseproject.entity.Champion;
import org.codenova.slseproject.repository.ChampionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class HomeAPI {

    ChampionRepository championRepository;

    @GetMapping("/sort")
    public List<Champion> sort(@RequestParam("type") String type, @RequestParam("sort") String sort) {

        if (type.equals("name") && sort.equals("asc")) {
            return championRepository.selectNameAsc();
        } else if (type.equals("name") && sort.equals("desc")) {
            return championRepository.selectNameDesc();
        } else if (type.equals("price") && sort.equals("asc")) {
            return championRepository.selectPriceAsc();
        } else if (type.equals("price") && sort.equals("desc")) {
            return championRepository.selectPriceDesc();
        } else {
            return championRepository.findAll();
        }
    }

    @GetMapping("/api/champion/detail")
    public Champion getChampionDetail(@RequestParam("name") String name) {
        return championRepository.findByName(name);
    }

}
