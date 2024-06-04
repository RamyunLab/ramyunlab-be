package ramyunlab_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ramyunlab_be.dto.UserDTO;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.security.TokenProvider;
import ramyunlab_be.service.UserService;

@Controller
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO){
        try {
            UserEntity user = UserEntity.builder()
                .userId(userDTO.getUserId())
                .nickname(userDTO.getNickname())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

            UserEntity registeredUser = userService.create(user);
            UserDTO responseUserDTO = userDTO.builder()
                .userId(registeredUser.getUserId())
                .nickname(registeredUser.getNickname())
                .idx(registeredUser.getIdx())
                .build();

            return ResponseEntity.ok().body(responseUserDTO.toString());

        }catch (Exception e){
            return ResponseEntity
                .badRequest()
                .body(e.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO) {
        try {

            UserEntity user = userService.getByCredentials(userDTO.getUserId(), userDTO.getPassword());

            // user 가 있으면 토큰 제공
            if (user != null) {
                String token = tokenProvider.create(user);
                final UserDTO responseUserDTO = UserDTO.builder()
                    .userId(user.getUserId())
                    .idx(user.getIdx())
                    .token(token)
                    .build();

                return ResponseEntity.ok().body(responseUserDTO.toString());
            } else {
                return ResponseEntity
                    .badRequest()
                    .body("login failed");
            }
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(e.getMessage());
        }
    }
}
