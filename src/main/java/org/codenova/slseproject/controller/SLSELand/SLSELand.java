package org.codenova.slseproject.controller.SLSELand;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SLSELand {

    @GetMapping("/slseland")
    public String showPage() {
        return "SLSELand/slseland"; // templates/slse-land.html 렌더링
    }
}