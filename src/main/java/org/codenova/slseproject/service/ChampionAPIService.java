package org.codenova.slseproject.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codenova.slseproject.response.ChampionResponse;
import org.codenova.slseproject.entity.Champion;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChampionAPIService {

    public Champion[] findAllChampion() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        String url = "https://ddragon.leagueoflegends.com/cdn/15.7.1/data/ko_KR/champion.json";

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                String.class
        );

        ChampionResponse championResponse = objectMapper.readValue(response.getBody(), ChampionResponse.class);
        Champion[] champions = championResponse.getData().values().toArray(new Champion[0]);

        // 이미지 경로 및 스탯 추가
        for (Champion champion : champions) {
            champion.setImageUrl("https://ddragon.leagueoflegends.com/cdn/15.7.1/img/champion/" + champion.getId() + ".png");

            // 스탯 json url
            String detailUrl = "https://ddragon.leagueoflegends.com/cdn/15.7.1/data/ko_KR/champion/" + champion.getId() + ".json";

            ResponseEntity<String> detailResponse = restTemplate.exchange(
                    detailUrl,
                    HttpMethod.GET,
                    null,
                    String.class
            );

            JsonNode root = objectMapper.readTree(detailResponse.getBody());
            JsonNode stats = root.path("data").path(champion.getId()).path("stats");

            int hp = stats.path("hp").asInt();
            int attackdamage = stats.path("attackdamage").asInt();

            champion.setPrice(hp * attackdamage);
        }

        return champions;
    }

}