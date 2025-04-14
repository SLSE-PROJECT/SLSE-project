package org.codenova.slseproject.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.repository.UserRepository;
import org.codenova.slseproject.request.EmailCheck;
import org.codenova.slseproject.service.GoogleApi;
import org.codenova.slseproject.service.KakaoApi;
import org.codenova.slseproject.service.NaverApi;
import org.codenova.slseproject.vo.GoogleTokenResponse;
import org.codenova.slseproject.vo.KakaoTokenResponse;
import org.codenova.slseproject.vo.NaverProfileResponse;
import org.codenova.slseproject.vo.NaverTokenResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class Auth {

    private UserRepository userRepository;
    private KakaoApi kakaoApiService;
    private NaverApi naverApiService;
    private GoogleApi googleApiService;

    @GetMapping("/signup")
    public String signup() {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signupPost(@ModelAttribute User user, @ModelAttribute @Valid EmailCheck email, BindingResult result) {
        if (result.hasErrors()) {
            return "auth/signup";
        }
        user.setEmail(email.getEmail());
        userRepository.create(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginHandle(Model model) {


        model.addAttribute("kakaoClientId", "9d7d57ad6e80992d91fff47b4240e032");
        model.addAttribute("kakaoRedirectUri", "http://192.168.10.96:8080/auth/kakao/callback");


        model.addAttribute("naverClientId", "cs0eCk_B4O1jRJH2A4OY");
        model.addAttribute("naverRedirectUri", "http://192.168.10.96:8080/auth/naver/callback");


        return "auth/login";
    }


    @PostMapping("/login")
    public String loginPost(@RequestParam("password") String password,
                            @ModelAttribute @Valid EmailCheck email, BindingResult result,
                            Model m, HttpSession session, HttpServletResponse response) {

        if (result.hasErrors()) {
            return "auth/login";
        }

        User user = userRepository.selectByEmail(email.getEmail());
        if (user == null) {
            return "auth/login";
        } else if (!user.getPassword().equals(password)) {
            return "auth/login";
        }

        Cookie cookie = new Cookie("loginEmail", user.getEmail());
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 365 * 10);
        response.addCookie(cookie);

        session.setAttribute("user", user);
        return "redirect:/";
    }

    //=========================================================KAKAO====================================================
    @GetMapping("/kakao/callback")
    public String kakaoCallbackHandle(@RequestParam("code") String code,
                                      HttpSession session
    ) throws JsonProcessingException {
        log.info("카카오 콜백 - code: {}", code);

        // 1. 카카오 토큰 교환
        KakaoTokenResponse response = kakaoApiService.exchangeToken(code);
        log.info("KakaoTokenResponse: {}", response);

        // 2. idToken null 체크
        String idToken = response.getIdToken();
        if (idToken == null || idToken.isEmpty()) {
            log.error("id_token is missing in Kakao response");
            throw new IllegalStateException("Kakao id_token is null or empty");
        }

        // 3. JWT 디코딩
        DecodedJWT decodedJWT = JWT.decode(idToken);
        String sub = decodedJWT.getClaim("sub").asString();
        String nickname = decodedJWT.getClaim("nickname").asString(); // 카카오에서는 대부분 "nickname"이 아님. 확인 필요

        log.info("Decoded JWT - sub: {}, nickname: {}", sub, nickname);

        // 4. 사용자 조회 또는 생성
        User found = userRepository.selectByProviderAndProviderId("KAKAO", sub);
        if (found != null) {
            session.setAttribute("user", found);
        } else {
            User user = User.builder()
                    .provider("KAKAO")
                    .providerId(sub)
                    .nickname(nickname)
                    .build();
            userRepository.create(user);
            session.setAttribute("user", user);
        }

        return "redirect:/";
    }
    //==================================================================================================================

    @GetMapping("/naver/callback")
    public String naverCallbackHandle(@RequestParam("code") String code,
                                      @RequestParam("state") String state, HttpSession session) throws JsonProcessingException {
        // log.info("code = {}, state = {}", code, state);

        NaverTokenResponse tokenResponse =
                naverApiService.exchangeToken(code, state);

        // log.info("accessToken = {}", tokenResponse.getAccessToken());


        NaverProfileResponse profileResponse
                = naverApiService.exchangeProfile(tokenResponse.getAccessToken());
        log.info("profileResponse id = {}", profileResponse.getId());
        log.info("profileResponse nickname = {}", profileResponse.getNickname());

        User found = userRepository.selectByProviderAndProviderId("NAVER", profileResponse.getId());
        if (found == null) {
            User user = User.builder()
                    .nickname(profileResponse.getNickname())
                    .provider("NAVER")
                    .providerId(profileResponse.getId())
                    .build();

            userRepository.create(user);
            session.setAttribute("user", user);

        } else {
            session.setAttribute("user", found);
        }
        return "redirect:/index";
    }
    /*
    @GetMapping("/google/callback")
    public String googleCallback(@RequestParam String code, HttpSession session, HttpServletResponse response) {
        try {

            GoogleTokenResponse tokenResponse = googleApiService.exchangeToken(code);
            log.info("Access Token: {}", tokenResponse.getAccessToken());

            String accessToken = tokenResponse.getAccessToken();

            User user = userRepository.selectByEmail(accessToken);
            if (user == null) {
                user = new User();
                user.setEmail(accessToken);
                userRepository.create(user);
            }

            Cookie cookie = new Cookie("loginEmail", user.getEmail());
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 365 * 10);
            response.addCookie(cookie);

            session.setAttribute("user", user);

            return "redirect:/";
        } catch (Exception e) {
            log.error("Google OAuth error", e);
            return "redirect:/auth/login";
        }



    }

     */
}
