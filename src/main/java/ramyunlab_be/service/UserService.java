package ramyunlab_be.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.repository.UserRepository;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity){
        if(userEntity == null ||
            userEntity.getUserId() == null || userEntity.getUserId().trim().isEmpty() ||
            userEntity.getNickname() == null || userEntity.getNickname().trim().isEmpty() ||
            userEntity.getPassword() == null || userEntity.getPassword().trim().isEmpty()){
            throw new RuntimeException("Invalid arguments : 빈 칸을 입력해주세요.");
        }   // 회원가입 빈칸 여부 확인

        final String userId = userEntity.getUserId();
        final String nickname = userEntity.getNickname();

        // 아이디 중복 체크
        if(userRepository.existsByUserId(userId)){
            log.warn("이미 존재하는 아이디입니다. {}", userId);
            throw new RuntimeException("userId already exists");
        }

        // 닉네임 중복 체크
        if(userRepository.existsByNickname(nickname)){
            log.warn("이미 존재하는 닉네임입니다. {}", nickname);
            throw new RuntimeException("nickname already exists");
        }
        return userRepository.save(userEntity);
    }
}
