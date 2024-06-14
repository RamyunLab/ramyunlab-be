package ramyunlab_be.repository;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ramyunlab_be.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByUserId(String userId);

    Boolean existsByNickname(String nickname);

//    UserEntity findByUserId(String userId);

//    @Query("SELECT u FROM UserEntity u WHERE u.userId = :userId")
    UserEntity findByUserId(String userId);

    @Query("SELECT u FROM UserEntity u WHERE u.userId LIKE %:keyword%")
    List<UserEntity> searchByUserId(@Param("keyword") String keyword);
//    @Query("SELECT u FROM UserEntity u Where u.userIdx = :userIdx")
    Optional<UserEntity> findByUserIdx(Long userIdx);
//    Optional<UserEntity> findByUserIdx2(Long userIdx);

//    UserEntity findByUserIdAndPassword(String userId, String password);

}
