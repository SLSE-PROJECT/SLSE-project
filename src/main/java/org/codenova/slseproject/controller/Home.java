package org.codenova.slseproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.codenova.slseproject.entity.Champion;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.repository.UserRepository;
import org.codenova.slseproject.service.ChampionAPIService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class Home {

    private final ChampionAPIService championAPIService;
    private final UserRepository userRepository;

    public Home(ChampionAPIService championAPIService, UserRepository userRepository) {
        this.championAPIService = championAPIService;
        this.userRepository = userRepository;
    }

    @RequestMapping("/")
    public String home(HttpSession session, HttpServletRequest request, Model m) throws JsonProcessingException {

        if (session.getAttribute("user") == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("loginEmail".equals(cookie.getName())) {
                        String value = cookie.getValue();
                        User user = null;

                        if (value.contains(":")) {

                            String[] parts = value.split(":");
                            String provider = parts[0];
                            String providerId = parts[1];
                            user = userRepository.selectByProviderAndProviderId(provider, providerId);
                        } else {

                            user = userRepository.selectByEmail(value);
                        }

                        if (user != null) {
                            session.setAttribute("user", user);
                        }
                        break;
                    }
                }
            }
        }

        Champion[] champions = championAPIService.findAllChampion();
        List<Champion> championList = Arrays.asList(champions);

        m.addAttribute("champions", championList);

        return "home";
    }

    @GetMapping("/auth/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();


        Cookie cookie = new Cookie("loginEmail", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }
}
