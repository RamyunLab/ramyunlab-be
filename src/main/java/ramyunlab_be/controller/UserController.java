package ramyunlab_be.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.dto.UserDTO;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.service.UserService;
import ramyunlab_be.vo.StatusCode;

@RestController
@RequestMapping(value="/api", produces="application/json; charset=utf8")
@Slf4j
@Tag(name = "User", description = "사용자 관련 API")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "회원탈퇴", description = "RequestBody : 패스워드 입력, 자물쇠 모양 아이콘 누르고 Available authorizations 에 token 정보 ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "탈퇴 성공"),
        @ApiResponse(responseCode = "400")
    })
    @DeleteMapping("/user")
    public ResponseEntity<ResDTO> deleteUser(
        @AuthenticationPrincipal String userIdx,
        @Valid
        @RequestBody UserDTO userDTO
    ) {
//        try {
            log.warn("userId {}, userDTO {} ", userIdx, userDTO.getPassword());
            UserEntity user = userService.delete(Long.valueOf(userIdx), userDTO.getPassword());


            return ResponseEntity.ok().body(ResDTO
                .builder()
                .statusCode(StatusCode.OK)
//                .data(user.getUserId())
                .message("탈퇴 성공")
                .build());
//        } catch (RuntimeException e) {
//            if (e.getMessage().equals("User doesn't exist")) {
//                return ResponseEntity
//                    .badRequest()
//                    .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST)
//                        .message("user doesn't exist").build());
//            } else if (e.getMessage().equals("Invalid password")) {
//                return ResponseEntity
//                    .badRequest()
//                    .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST)
//                        .message("Invalid password").build());
//            } else {
//                return ResponseEntity
//                    .badRequest()
//                    .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST)
//                        .message("withdrawal failed").build());
//            }
//        }

    }

        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ResDTO> handleValidationException(ValidationException e) {
            return ResponseEntity
                .badRequest()
                .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message(e.getMessage()).build());
        }
}
