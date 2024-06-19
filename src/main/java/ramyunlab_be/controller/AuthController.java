package ramyunlab_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.dto.UserDTO;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.vo.StatusCode;
import ramyunlab_be.security.TokenProvider;
import ramyunlab_be.service.UserService;

@RestController
@RequestMapping(value = "/auth", produces = "application/json; charset=utf8")
@Tag(name = "Auth", description = "로그인, 회원가입 관련 API")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    final private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    public AuthController(final UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "회원가입", description = " requestBody : 아이디, 닉네임, 패스워드 입력 ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입 성공"),
        @ApiResponse(responseCode = "400")
    })
    @PostMapping("/register")
    public ResponseEntity<ResDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {

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

    @Operation(summary = "로그인", description = "requsetBody : 아이디, 패스워드 입력")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "400")
    })
    @PostMapping("/login")
    public ResponseEntity<ResDTO> loginUser(@Valid @RequestBody UserDTO userDTO) {
        UserEntity user = userService.getByCredentials(userDTO.getUserId(), userDTO.getPassword());
        // user 가 있으면 토큰 제공
        if (user != null) {
            String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                .userId(user.getUserId())
                .userIdx(user.getUserIdx())
                .token(token)
                .build();

            if (responseUserDTO.getUserDeletedAt() != null) {
                throw new ValidationException("이미 탈퇴한 회원입니다.");
            }

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
    }

    @Operation(summary = "아이디 중복검사", description = " RequestBody : 아이디 입력, 자물쇠 모양 아이콘 누르고 Available authorizations 에 token 정보 ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "중복 체크 성공"),
        @ApiResponse(responseCode = "400")
    })
    @PostMapping("/checkId")
    public ResponseEntity<ResDTO> checkId(@Valid @RequestBody UserDTO userDTO) {
        UserEntity user = UserEntity.builder()
            .userId(userDTO.getUserId())
            .build();

        UserDTO checkedUser = userService.checkId(user);

        return ResponseEntity.ok().body(ResDTO.builder().statusCode(StatusCode.OK).data(checkedUser).message("중복 체크 성공").build());
    }

    @Operation(summary = "닉네임 중복검사", description = "RequestBody : 닉네임 입력, 자물쇠 모양 아이콘 누르고 Available authorizations 에 token 정보 \"")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "중복 체크 성공"),
        @ApiResponse(responseCode = "400")
    })
    @PostMapping("/checkNickname")
    public ResponseEntity<ResDTO> checkNickname(@Valid @RequestBody UserDTO userDTO) {
        UserEntity user = UserEntity.builder()
            .nickname(userDTO.getNickname())
            .build();

        UserDTO checkedUser = userService.checkNickname(user);
        return ResponseEntity.ok().body(ResDTO.builder().statusCode(StatusCode.OK).data(checkedUser).message("중복 체크 성공").build());
    }

    @GetMapping("/kakao")
    public ResponseEntity<String> socialLogin() {
        String kakaoAuthUrl = userService.getKakaoAuthUrl();
        return ResponseEntity.ok().body(kakaoAuthUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<ResDTO> callback(String code) throws Exception {
        UserEntity user = userService.handleKakaoCallback(code);
        if (user != null) {
            if (user.getUserDeletedAt() != null) {
                throw new ValidationException("이미 탈퇴한 회원입니다.");
            }
            String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                .userId(user.getUserId())
                .userIdx(user.getUserIdx())
                .token(token)
                .build();

            return ResponseEntity.ok().body(ResDTO.builder()
                .statusCode(StatusCode.OK)
                .message("소셜 로그인 성공")
                .data(responseUserDTO)
                .build());
        } else {
            return ResponseEntity
                .badRequest()
                .body(ResDTO.builder()
                    .statusCode(StatusCode.BAD_REQUEST)
                    .message("사용자 정보를 찾을 수 없습니다.")
                    .build());
        }
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResDTO> handleValidationException(ValidationException e) {
        return ResponseEntity
            .badRequest()
            .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message(e.getMessage()).build());
    }
}