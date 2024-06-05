package ramyunlab_be.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ramyunlab_be.dto.UserDTO;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.repository.UserRepository;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserEntity create(final UserEntity userEntity){
        if(userEntity == null ||
            userEntity.getUserId() == null || userEntity.getUserId().trim().isEmpty() ||
            userEntity.getNickname() == null || userEntity.getNickname().trim().isEmpty() ||
            userEntity.getPassword() == null || userEntity.getPassword().trim().isEmpty()){
            throw new RuntimeException("Invalid arguments : 빈 칸을 입력해주세요.");
        }   // 회원가입 빈칸 여부 확인

        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String userId, final String password){
        if(userId == null || password == null || userId.trim().isEmpty() || password.trim().isEmpty()){
            throw new RuntimeException("Invalid arguments : 빈 칸을 입력해주세요.");
        }

        UserEntity user = userRepository.findByUserId(userId);

        if(user != null && passwordEncoder.matches(password, user.getPassword())){
            return UserEntity.builder().userId(userId).build();
        }else return null;
    }

    public UserDTO checkId(UserEntity userEntity){
        if(userEntity == null ||
            userEntity.getUserId() == null ||
            userEntity.getUserId().trim().isEmpty()){
            throw new RuntimeException("Invalid arguments : 빈 칸을 입력해주세요.");
        }

        final String userId = userEntity.getUserId();

        // 아이디 중복 체크\

            if(userRepository.existsByUserId(userId)){
                log.warn("이미 존재하는 아이디입니다. {}", userId);
                throw new RuntimeException("userId already exists");
            }

            return UserDTO.builder().userId(userId).build();
    }

    public UserDTO checkNickname(UserEntity userEntity){
        if(userEntity == null ||
            userEntity.getNickname() == null ||
            userEntity.getNickname().trim().isEmpty()){
            throw new RuntimeException("Invalid arguments : 빈 칸을 입력해주세요.");
        }

        final String nickname = userEntity.getNickname();

        // 닉네임 중복 체크
            if(userRepository.existsByNickname(nickname)){
                log.warn("이미 존재하는 닉네임입니다. {}", nickname);
                throw new RuntimeException("nickname already exists");
            }

            return UserDTO.builder().nickname(nickname).build();
    }

    public UserEntity delete(final Long idx, final String password){
        UserEntity user = userRepository.findByIdx(idx)
            .orElseThrow(() -> new RuntimeException("user doesn't exist"));

        if(user != null && passwordEncoder.matches(password, user.getPassword())){
            userRepository.delete(user);
            return user;
        } else{
            throw new RuntimeException("Invalid password");
        }
    }
}
