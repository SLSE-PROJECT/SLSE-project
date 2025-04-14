package org.codenova.slseproject.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.repository.UserRepository;
import org.codenova.slseproject.request.EmailCheck;
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
public class Auth {

    private UserRepository userRepository;
    private KakaoApi kakaoApiService;
    private NaverApi naverApiService;

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
    public void login() {
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
    public String kakaoCallBack(@RequestParam("code") String code,
                                HttpSession session
    ) throws JsonProcessingException {

        KakaoTokenResponse response =kakaoApiService.exchangeToken(code);

        DecodedJWT decodedJWT = JWT.decode(response.getIdToken());

        String sub = decodedJWT.getClaim("sub").asString();
        String nickname = decodedJWT.getClaim("nickname").asString();
        String picture = decodedJWT.getClaim("picture").asString();

        User found = userRepository.selectByProviderAndProviderId("kakao", sub);
        if(found != null){
            session.setAttribute("user", found);
        }else{
            found = User.builder().provider("kakao")
                    .providerId(sub).nickname(nickname).build();

            userRepository.create(found);
            session.setAttribute("user", found);
        }

        return "redirect:/";
    }
    //==================================================================================================================

    //=========================================================NAVER====================================================
    @GetMapping("/naver/callback")
    public String naverCallBack(@RequestParam("code") String code,
                                @RequestParam("state") String state,
                                HttpSession session)
            throws JsonProcessingException {

        NaverTokenResponse response = naverApiService.exchageToken(code, state);

        NaverProfileResponse profileResponse = naverApiService.exchageProfile(response.getAccessToken());
        String id = profileResponse.getId();
        String nickname = profileResponse.getNickname();
        String profileImage = profileResponse.getProfileImage();

        User found = userRepository.selectByProviderAndProviderId("naver", id);
        if(found != null){
            session.setAttribute("user", found);
        }else{
            found = User.builder().provider("naver")
                    .providerId(id).nickname(nickname).build();

            userRepository.create(found);
            session.setAttribute("user", found);
        }

        return "redirect:/";
    }
    //==================================================================================================================
}
