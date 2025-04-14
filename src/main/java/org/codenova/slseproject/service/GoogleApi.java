package org.codenova.slseproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codenova.slseproject.vo.GoogleTokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleApi {

    private final ObjectMapper objectMapper;

    public GoogleTokenResponse exchangeToken(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", "236234509816-hl14u36t0rguipsr5l81vg2776rv6ubr.apps.googleusercontent.com");
        body.add("client_secret", "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");
        body.add("redirect_uri", "http://192.168.10.152:8080/auth/google/callback");
        body.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                request,
                String.class
        );

        log.info("Google Token Response: {}", response.getBody());

        return objectMapper.readValue(response.getBody(), GoogleTokenResponse.class);
    }
}
