package ramyunlab_be.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ramyunlab_be.dto.UserDTO;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class MainController {

    List<UserDTO> userList = new ArrayList<>();
    @Autowired
    private UserService userService;

    @DeleteMapping("/user/{idx}")
    public ResponseEntity<String> deleteUser(
        @PathVariable Long idx,
        @RequestBody UserDTO userDTO
    ) {
        try {
            log.info("hihihi 1 , {}", idx);
            UserEntity user = userService.delete(idx, userDTO.getPassword());
            log.warn("hihihi 2, {}", idx);
                return ResponseEntity.ok().body("withdrawal success");
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User doesn't exist")) {
                return ResponseEntity.badRequest().body("User doesn't exist");
            } else if (e.getMessage().equals("Invalid password")) {
                return ResponseEntity.badRequest().body("Invalid password");
            } else {
                return ResponseEntity.badRequest().body("Withdrawal failed");
            }
        }
    }
}
