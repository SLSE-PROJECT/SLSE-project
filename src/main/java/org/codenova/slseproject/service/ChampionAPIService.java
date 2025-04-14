package org.codenova.slseproject.service;
import com.fasterxml.jackson.core.JsonProcessingException;
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

        String url = "https://ddragon.leagueoflegends.com/cdn/15.7.1/data/ko_KR/champion.json";

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();

        ChampionResponse championResponse = objectMapper.readValue(response.getBody(), ChampionResponse.class);

        Champion[] champions = championResponse.getData().values().toArray(new Champion[0]);

        // 이미지 경로 셋팅
        for (Champion champion : champions) {
            champion.setImageUrl("https://ddragon.leagueoflegends.com/cdn/15.7.1/img/champion/" + champion.getId() + ".png");
        }

        return champions;
    }
}