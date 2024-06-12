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
}
