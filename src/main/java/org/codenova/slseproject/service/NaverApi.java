package org.codenova.slseproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codenova.slseproject.vo.NaverProfileResponse;
import org.codenova.slseproject.vo.NaverTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
public class NaverApi {

    public ObjectMapper objectMapper;

    public NaverTokenResponse exchageToken(String code, String state) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        String tokenUrl = "https://nid.naver.com/oauth2.0/token" +
                "?grant_type=authorization_code" +
                "&client_id=" + "6iJXszxw2uQsytVgl6Qx" +
                "&client_secret=" + "hFhpGamG5Q" +
                "&code=" + code +
                "&state=" + state;

        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, null, String.class);

        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        return objectMapper.readValue(response.getBody(), NaverTokenResponse.class);
    }

    public NaverProfileResponse exchageProfile(String accessToken) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        System.out.println("1");

        ResponseEntity<String> response = restTemplate.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.GET, entity, String.class);

        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        String extractJson = objectMapper.readTree(response.getBody()).path("response").toString();

        return objectMapper.readValue(extractJson, NaverProfileResponse.class);

    }
}
