package ramyunlab_be.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/admin", produces="application/json; charset=utf8")
@Tag(name = "Admin", description = "관리자 관련 API")
public class AdminUserController {
}
