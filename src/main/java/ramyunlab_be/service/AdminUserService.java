package ramyunlab_be.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.repository.UserRepository;

@Service
@Slf4j
public class AdminUserService {

    static private UserRepository userRepository = null;

    @Autowired
    public AdminUserService(final UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public static Page<UserEntity> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);

    }

    public UserEntity deleteUser(final Long userIdx,
                                 final String admin){
        userRepository.findByUserIdx(Long.valueOf(admin))
           .orElseThrow(() -> new RuntimeException("로그인을 해주세요."));

        UserEntity user = userRepository.findByUserIdx(userIdx).orElseThrow(() -> new RuntimeException("관리자로 로그인을 진행해주세요."));


        userRepository.delete(user);
        return user;
    }
}
