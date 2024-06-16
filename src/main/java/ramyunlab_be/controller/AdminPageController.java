package ramyunlab_be.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {
    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("/userPage")
    public String adminUserPage(Model model,
                                @AuthenticationPrincipal String userIdx) {
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
