package ramyunlab_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.service.AdminGoodsService;
import ramyunlab_be.service.AdminUserService;
import ramyunlab_be.vo.StatusCode;

@Slf4j
@RestController
@RequestMapping(value = "/admin", produces="application/json; charset=utf8")
@Tag(name = "Admin", description = "관리자 관련 API")
public class AdminUserController {

    final private AdminUserService adminUserService;

    @Autowired
    public AdminUserController(final AdminUserService adminUserService){
        this.adminUserService = adminUserService;
    }

    @Operation(summary = "사용자 목록 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "사용자 목록 조회 실패")
    })
    @GetMapping("/users")
    public ResponseEntity<ResDTO> getUsers(Pageable pageable) {
        Page<UserEntity> results = AdminUserService.getUsers(pageable);

        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .data(results)
            .message("사용자 목록 호출 완료")
            .build());
    }
}
