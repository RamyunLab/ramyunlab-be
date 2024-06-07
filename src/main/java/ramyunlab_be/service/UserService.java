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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserEntity create(final UserEntity userEntity){
//        if(userEntity == null ||
//            userEntity.getUserId() == null || userEntity.getUserId().trim().isEmpty() ||
//            userEntity.getNickname() == null || userEntity.getNickname().trim().isEmpty() ||
//            userEntity.getPassword() == null || userEntity.getPassword().trim().isEmpty()){
//            throw new RuntimeException("Invalid arguments : 빈 칸을 입력해주세요.");
//        }   // 회원가입 빈칸 여부 확인
//        try{
        checkId(userEntity);
        checkNickname(userEntity);
//        }catch (Exception e){
//            throw new RuntimeException("아이디 중복검사를 진행해주세요.");
//        }

        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String userId, final String password){
//        if(userId == null || password == null || userId.trim().isEmpty() || password.trim().isEmpty()){
//            throw new RuntimeException("Invalid arguments : 빈 칸을 입력해주세요.");
//        }

        UserEntity user = userRepository.findByUserId(userId);

        log.warn("user create service {}, {}, {}, {}", userId, user.getUserId(), password, user.getPassword());

        if(user != null && passwordEncoder.matches(password, user.getPassword())){
            return UserEntity.builder().userId(userId).userIdx(user.getUserIdx()).build();
        }else if(user == null){
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        }else if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        } else {
            return null;
        }

    }

    public UserDTO checkId(UserEntity userEntity) {
//        if(userEntity == null ||
//            userEntity.getUserId() == null ||
//            userEntity.getUserId().trim().isEmpty()){
//            throw new RuntimeException("Invalid arguments : 빈 칸을 입력해주세요.");
//        }

        final String userId = userEntity.getUserId();

        // 아이디 중복 체크\
            if(userRepository.existsByUserId(userId)){
                log.warn("이미 존재하는 아이디입니다. {}", userId);
                throw new RuntimeException("userId already exists");
            }
        return UserDTO.builder().userId(userId).build();

    }

    public UserDTO checkNickname(UserEntity userEntity){
//        if(userEntity == null ||
//            userEntity.getNickname() == null ||
//            userEntity.getNickname().trim().isEmpty()){
//            throw new RuntimeException("Invalid arguments : 빈 칸을 입력해주세요.");
//        }

        final String nickname = userEntity.getNickname();

        // 닉네임 중복 체크
            if(userRepository.existsByNickname(nickname)){
                log.warn("이미 존재하는 닉네임입니다. {}", nickname);
                throw new RuntimeException("nickname already exists");
            }

            return UserDTO.builder().nickname(nickname).build();
    }

    public UserEntity delete(final Long userIdx, final String password){
        UserEntity user = userRepository.findByUserIdx(userIdx)
            .orElseThrow(() -> new RuntimeException("로그인을 해주세요."));



        if(user != null && passwordEncoder.matches(password, user.getPassword())){
            userRepository.delete(user);
//            Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
//            UserEntity deletedUser = user.toBuilder()
//                    .userIdx(user.getUserIdx())
//                        .userDeletedAt(currentTimestamp)
//                            .build();
//            userRepository.save(deletedUser);
            return user;
        } else if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        } else throw new RuntimeException("회원탈퇴 실패");
    }
}
