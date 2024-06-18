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

    final private UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserEntity create(final UserEntity userEntity){

        checkId(userEntity);
        checkNickname(userEntity);


        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String userId, final String password){


        UserEntity user = userRepository.findByUserId(userId);
        Timestamp userDeletedAt = userRepository.findUserDeletedAtByUserId(userId);
        log.warn("user create service {}, {}, {}, {}", userId, user.getUserId(), password, user.getPassword());

        if (userDeletedAt != null){
            throw new RuntimeException("탈퇴한 회원입니다.");
        }else if(user != null && passwordEncoder.matches(password, user.getPassword())){
            return UserEntity.builder().userId(userId).userIdx(user.getUserIdx()).build();
        }else if(user == null){
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        }else if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }
         return null;
        }



    public UserDTO checkId(UserEntity userEntity) {

        final String userId = userEntity.getUserId();

        // 아이디 중복 체크\
            if(userRepository.existsByUserId(userId)){
                log.warn("이미 존재하는 아이디입니다. {}", userId);
                throw new RuntimeException("userId already exists");
            }
        return UserDTO.builder().userId(userId).build();

    }

    public UserDTO checkNickname(UserEntity userEntity){

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
            return user;
        } else if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        } else throw new RuntimeException("회원탈퇴 실패");
    }
    
    // 닉네임 변경
    public UserEntity updateNickname(String userIdx, UserDTO userDTO) {
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx))
                .orElseThrow(() -> new RuntimeException("Error shit;;"));
        user = user.toBuilder()
                .nickname(userDTO.getNickname())
                .build();
        return userRepository.save(user);
    }

    // 비밀번호 변경 시 비밀번호 확인.
    public boolean confirmPassword(String userIdx, UserDTO userDTO) {
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx))
                .orElseThrow(()->new RuntimeException("회원 정보 없음."));

        boolean isMatched = passwordEncoder.matches(userDTO.getPassword(),user.getPassword());

        return isMatched;
    }

    // 비밀번호 변경
    public UserEntity updatePassword(String userIdx, UserDTO userDTO) {
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx))
                .orElseThrow(()-> new RuntimeException("회원 정보 없음."));

        user = user.toBuilder()
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();
        return userRepository.save(user);
    }
}
