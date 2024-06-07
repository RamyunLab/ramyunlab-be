package ramyunlab_be.controller;


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
public class UserController {

    @Autowired
    private UserService userService;

    @DeleteMapping("/user")
    public ResponseEntity<ResDTO> deleteUser(
        @AuthenticationPrincipal String userIdx,
        @Valid
        @RequestBody UserDTO userDTO
    ) {
//        try {
//            log.warn("userId {}, userDTO {} ", userIdx, userDTO.getPassword());
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
