package org.codenova.slseproject.controller.SLSELand;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.repository.RouletteRepository;
import org.codenova.slseproject.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class SLSELand {

    private UserRepository userRepository;
    private RouletteRepository rouletteRepository;

    @GetMapping("/slseland")
    public String showPage(Model m, HttpSession session, HttpServletRequest request) {
        User user = (User) session.getAttribute("user");

        // 세션에 유저 없으면 쿠키로 자동 로그인 시도
        if (user == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("loginEmail".equals(cookie.getName())) {
                        String value = cookie.getValue();

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

        // 여전히 user 없으면 로그인 페이지로 리다이렉트
        if (user == null) {
            return "redirect:/auth/login";
        }

        // 👉 총 적립금
        int pointPoolAmount = rouletteRepository.getTotalAmount();

        // 👉 유저 쿠폰 개수
        int userCouponCount = rouletteRepository.countUserCoupons(user.getId());

        m.addAttribute("user", user);
        m.addAttribute("pointPool", pointPoolAmount);
        m.addAttribute("couponCount", userCouponCount);
        return "SLSELand/slseland";
    }

}