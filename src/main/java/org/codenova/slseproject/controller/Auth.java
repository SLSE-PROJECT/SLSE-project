package org.codenova.slseproject.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.repository.UserRepository;
import org.codenova.slseproject.request.EmailCheck;
import org.codenova.slseproject.service.ChampionService;
import org.codenova.slseproject.service.GoogleApi;
import org.codenova.slseproject.service.KakaoApi;
import org.codenova.slseproject.service.NaverApi;
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
    private ChampionService championService;

    @GetMapping("/signup")
    public String signup() {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signupPost(@ModelAttribute User user, @ModelAttribute @Valid EmailCheck email, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/signup";
        }

        if (!email.getEmail().contains("@")) {
            model.addAttribute("emailError", "이메일 형식이 올바르지 않습니다.");
            return "auth/signup";
        }

        if (userRepository.selectByEmail(email.getEmail()) != null) {
            model.addAttribute("emailError", "이미 가입된 이메일입니다.");
            return "auth/signup";
        }

        if (!user.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
            model.addAttribute("passwordError", "비밀번호는 영문 대소문자와 숫자를 포함해 8자 이상이어야 합니다.");
            return "auth/signup";
        }


        user.setEmail(email.getEmail());
        userRepository.create(user);
        System.out.println(user.getId());
        championService.getOrCreateUserChampions(user);
        return "redirect:/";
    }
    @GetMapping("/api/auth/nickname")
    @ResponseBody
    public String checkNickname(@RequestParam String nickname) {
        User user = userRepository.selectByNickname(nickname);
        if (user != null) {
            return "duplicated";
        }
        return "available";
    }

    @GetMapping("/api/auth/available")
    @ResponseBody
    public String checkEmail(@RequestParam String email) {
        if (!email.contains("@")) {
            return "not-email";
        }

        if (userRepository.selectByEmail(email) != null) {
            return "duplicated";
        }

        return "available";
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
            System.out.println("Email");
            return "auth/login";
        }

        User user = userRepository.selectByEmail(email.getEmail());
        if (user == null || !user.getPassword().equals(password)) {
            System.out.println("password");
            return "auth/login";
        }

        Cookie cookie = new Cookie("loginEmail", user.getEmail());
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 365 * 10); // 10년
        response.addCookie(cookie);

        session.setAttribute("user", user);
        return "redirect:/";
    }

    // ✅ 카카오 로그인 콜백
    @GetMapping("/kakao/callback")
    public String kakaoCallbackHandle(@RequestParam("code") String code,
                                      HttpSession session,
                                      HttpServletResponse response) throws JsonProcessingException {
        KakaoTokenResponse responseData = kakaoApiService.exchangeToken(code);
        String idToken = responseData.getIdToken();

        DecodedJWT decodedJWT = JWT.decode(idToken);
        String sub = decodedJWT.getClaim("sub").asString();
        String nickname = decodedJWT.getClaim("nickname").asString();

        User found = userRepository.selectByProviderAndProviderId("KAKAO", sub);
        if (found != null) {
            session.setAttribute("user", found);

            Cookie cookie = new Cookie("loginEmail", "KAKAO:" + found.getProviderId());
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 365 * 10);
            response.addCookie(cookie);
        } else {
            User user = User.builder()
                    .provider("KAKAO")
                    .providerId(sub)
                    .nickname(nickname)
                    .build();
            userRepository.create(user);
            session.setAttribute("user", user);
            championService.getOrCreateUserChampions(user);

            Cookie cookie = new Cookie("loginEmail", "KAKAO:" + user.getProviderId());
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 365 * 10);
            response.addCookie(cookie);
        }

        return "redirect:/";
    }

    @GetMapping("/naver/callback")
    public String naverCallbackHandle(@RequestParam("code") String code,
                                      @RequestParam("state") String state,
                                      HttpSession session,
                                      HttpServletResponse response) throws JsonProcessingException {
        NaverTokenResponse tokenResponse = naverApiService.exchangeToken(code, state);
        NaverProfileResponse profile = naverApiService.exchangeProfile(tokenResponse.getAccessToken());

        User found = userRepository.selectByProviderAndProviderId("NAVER", profile.getId());
        if (found != null) {
            session.setAttribute("user", found);

            Cookie cookie = new Cookie("loginEmail", "NAVER:" + found.getProviderId());
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 365 * 10);
            response.addCookie(cookie);
        } else {
            User user = User.builder()
                    .provider("NAVER")
                    .providerId(profile.getId())
                    .nickname(profile.getNickname())
                    .build();
            userRepository.create(user);
            session.setAttribute("user", user);
            championService.getOrCreateUserChampions(user);

            Cookie cookie = new Cookie("loginEmail", "NAVER:" + user.getProviderId());
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 365 * 10);
            response.addCookie(cookie);
        }

        return "redirect:/";
    }
}