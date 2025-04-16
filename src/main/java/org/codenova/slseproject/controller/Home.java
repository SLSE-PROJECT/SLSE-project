package org.codenova.slseproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.codenova.slseproject.entity.Champion;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.repository.ChampionRepository;
import org.codenova.slseproject.repository.UserRepository;
import org.codenova.slseproject.service.ChampionAPIService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
@AllArgsConstructor
public class Home {

    private UserRepository userRepository;
    private ChampionRepository championRepository;

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

        List<Champion> championList = championRepository.findAll();

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
    @ResponseBody
    @GetMapping("/api/champion/detail")
    public Champion getChampionDetail(@RequestParam("name") String name) {
        return championRepository.findByName(name);
    }
}
