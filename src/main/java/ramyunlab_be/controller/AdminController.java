package ramyunlab_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.service.AdminService;
import ramyunlab_be.vo.StatusCode;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/admin", produces="application/json; charset=utf8")
@Tag(name = "Admin", description = "관리자 관련 API")
public class AdminController {

    final private AdminService adminService;

    @Autowired
    public AdminController(final AdminService adminService){
        this.adminService = adminService;
    }


    @Operation(summary = "전체 상품 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 상품 리스트 조회 성공"),
            @ApiResponse(responseCode = "400", description = "전체 상품 리스트 조회 실패")
    })
    @GetMapping("/goods")
    public ResponseEntity<ResDTO> getGoodsList(Pageable pageable){
        Page<RamyunEntity> results = AdminService.getGoodsList(pageable);
        List<RamyunEntity> responseResult = results.stream()
            .map(result -> RamyunEntity.builder()
                .ramyunIdx(result.getRamyunIdx())
                .ramyunName(result.getRamyunName())
                .ramyunImg(result.getRamyunImg())
                .ramyunKcal(result.getRamyunKcal())
                .noodle(result.getNoodle())
                .isCup(result.getIsCup())
                .cooking(result.getCooking())
                .gram(result.getGram())
                .ramyunNa(result.getRamyunNa())
                .build())
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
                .message("전체 상품 리스트 조회 성공")
                .data(responseResult)
            .build());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResDTO> handleValidationException(ValidationException e) {
        return ResponseEntity
            .badRequest()
            .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message(e.getMessage()).build());
    }
}
