package ramyunlab_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.entity.ReviewEntity;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.mail.EmailMessage;
import ramyunlab_be.service.AdminGoodsService;
import ramyunlab_be.service.AdminUserService;
import ramyunlab_be.service.EmailService;
import ramyunlab_be.vo.StatusCode;

@Slf4j
@RestController
@RequestMapping(value = "/admin", produces="application/json; charset=utf8")
@Tag(name = "Admin", description = "관리자 관련 API")
public class AdminUserController {

    final private AdminUserService adminUserService;
    final private EmailService emailService;

    @Autowired
    public AdminUserController(final AdminUserService adminUserService,
                               final EmailService emailService){
        this.adminUserService = adminUserService;
        this.emailService = emailService;
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

    @Operation(summary = "사용자 삭제")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "사용자 삭제 실패")
    })
    @DeleteMapping("/user/{userIdx}")
    public ResponseEntity<ResDTO> deleteUser(@PathVariable Long userIdx,
                                             @AuthenticationPrincipal String admin){
        adminUserService.deleteUser(userIdx, admin);
        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .message("사용자 삭제 성공")
            .build());
    }

    @Operation(summary = "신고된 리뷰 목록 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "신고된 리뷰 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "신고된 리�� 목록 조회 실패")
    })
    @GetMapping("/reviews")
    public ResponseEntity<ResDTO> getReviews(Pageable pageable) {
        Page<ReviewEntity> results = AdminUserService.getReportedReview(pageable);
        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .data(results)
            .message("신고된 리뷰 목록 호출 완료")
            .build());
    }

    @Operation(summary = "신고된 리뷰 삭제")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "신고된 리뷰 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "신고된 리뷰 삭제 실패")
    })
    @DeleteMapping("/review/{rvIdx}")
    public ResponseEntity<ResDTO> deleteReview(@PathVariable Long rvIdx,
                                               @AuthenticationPrincipal String userIdx){
        adminUserService.deleteReportedReview(rvIdx, userIdx);
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .message("신고된 리뷰 삭제 성공")
           .build());
    }

    @Operation(summary = "메일 전송", description = "requestBody : 이메일, 아이디, 제목, 내용 / 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메일 전송 성공"),
        @ApiResponse(responseCode = "400", description = "메일 전송 실패")
    })
    @PostMapping("/mail")
    public ResponseEntity<ResDTO> sendMail(@RequestBody EmailMessage emailMessage,
                                           @AuthenticationPrincipal String userIdx)throws Exception{
        EmailMessage sendMessage = EmailMessage.builder()
            .userEmail(emailMessage.getUserEmail())
            .subject(emailMessage.getSubject())
            .userId(emailMessage.getUserId())
            .text(emailMessage.getText())
            .build();
        emailService.sendEmail(sendMessage, userIdx);

        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .message("메일 전송 성공")
           .build());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResDTO> handleValidationException(ValidationException e) {
        return ResponseEntity
            .badRequest()
            .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message(e.getMessage()).build());
    }
}
