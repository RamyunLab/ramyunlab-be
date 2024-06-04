package ramyunlab_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ramyunlab_be.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByUserId(String userId);

    Boolean existsByNickname(String nickname);

    UserEntity findByUserId(String userId);

    UserEntity findByUserIdAndPassword(String userId, String password);

}
