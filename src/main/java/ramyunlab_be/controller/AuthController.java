package ramyunlab_be.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.dto.UserDTO;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.vo.StatusCode;
import ramyunlab_be.security.TokenProvider;
import ramyunlab_be.service.UserService;

@RestController
@RequestMapping(value="/auth", produces="application/json; charset=utf8")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<ResDTO> registerUser(@Valid @RequestBody UserDTO userDTO){

            UserEntity user = UserEntity.builder()
                .userId(userDTO.getUserId())
                .nickname(userDTO.getNickname())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

            UserEntity registeredUser = userService.create(user);
            UserDTO responseUserDTO = userDTO.builder()
                .userId(registeredUser.getUserId())
                .nickname(registeredUser.getNickname())
                .userIdx(registeredUser.getUserIdx())
                .build();

            return ResponseEntity.ok().body(ResDTO
                .builder()
                .statusCode(StatusCode.OK)
                .data(responseUserDTO)
                .message("회원가입 성공")
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ResDTO> loginUser(@RequestBody UserDTO userDTO) {
        try {

            UserEntity user = userService.getByCredentials(userDTO.getUserId(), userDTO.getPassword());

            // user 가 있으면 토큰 제공
            if (user != null) {
                String token = tokenProvider.create(user);
                final UserDTO responseUserDTO = UserDTO.builder()
                    .userId(user.getUserId())
                    .userIdx(user.getUserIdx())
                    .token(token)
                    .build();

                return ResponseEntity.ok().body(ResDTO
                    .builder()
                    .statusCode(StatusCode.OK)
                    .data(responseUserDTO)
                    .message("로그인 성공")
                    .build());
            } else {
                return ResponseEntity
                    .badRequest()
                    .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).build());
            }
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).build());
        }
    }

    @PostMapping("/checkId")
    public ResponseEntity<ResDTO> checkId(@Valid @RequestBody UserDTO userDTO) {
            UserEntity user = UserEntity.builder()
                .userId(userDTO.getUserId())
                .build();

            UserDTO checkedUser = userService.checkId(user);

            return ResponseEntity.ok().body(ResDTO.builder().statusCode(StatusCode.OK).data(checkedUser).message("중복 체크 성공").build());
    }

    @PostMapping("/checkNickname")
    public ResponseEntity<ResDTO> checkNickname(@RequestBody UserDTO userDTO){
        try{
            UserEntity user = UserEntity.builder()
                .nickname(userDTO.getNickname())
                .build();

            UserDTO checkedUser = userService.checkNickname(user);
            return ResponseEntity.ok().body(ResDTO.builder().statusCode(StatusCode.OK).data(checkedUser).message("중복 체크 성공").build());
        }catch (Exception e){
            return ResponseEntity
                .badRequest()
                .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message(e.getMessage()).build());
        }
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResDTO> handleValidationException(ValidationException e) {
        return ResponseEntity
            .badRequest()
            .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message(e.getMessage()).build());
    }
}
