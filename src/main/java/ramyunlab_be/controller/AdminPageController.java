package ramyunlab_be.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class AdminPageController {
    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("/userPage")
    public String adminUserPage(Model model,
                                @AuthenticationPrincipal String userIdx) {
        log.warn("UserIDx {}", userIdx);
        return "adminUser";
    }

    @GetMapping("/goodsPage")
    public String adminGoodsPage(Model model,
                                @AuthenticationPrincipal String userIdx) {
        return "adminGoods";
    }

    @GetMapping("/brandPage")
    public String adminbrandPage(Model model,
                                @AuthenticationPrincipal String userIdx) {
        return "adminBrand";
    }


}
