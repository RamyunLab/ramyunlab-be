package ramyunlab_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ramyunlab_be.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByUserId(String userId);

    Boolean existsByNickname(String nickname);

//    @Query("SELECT u FROM UserEntity u WHERE u.userId = :userId")
    UserEntity findByUserId(String userId);

//    UserEntity findByUserIdAndPassword(String userId, String password);

}
