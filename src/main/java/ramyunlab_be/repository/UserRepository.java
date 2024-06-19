package ramyunlab_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ramyunlab_be.dto.RamyunProjection;
import ramyunlab_be.dto.UserProjection;
import ramyunlab_be.entity.UserEntity;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u.userIdx as userIdx, u.userId as userId, u.userNickname as userNickname, u.userDeletedAt as userDeletedAt, rv.reviews as review FROM UserEntity u JOIN u.reviews rv")
    Page<UserProjection> findAllUserswithReview(Pageable pageable);

    Boolean existsByUserId(String userId);

    Boolean existsByNickname(String nickname);

    @Query("SELECT u.userDeletedAt FROM UserEntity u WHERE u.userId = :userId")
    Timestamp findUserDeletedAtByUserId(String userId);

//    UserEntity findByUserId(String userId);

//    @Query("SELECT u FROM UserEntity u WHERE u.userId = :userId")
    UserEntity findByUserId(String userId);

    @Query("SELECT u FROM UserEntity u WHERE u.userId LIKE %:keyword%")
    List<UserProjection> searchByUserId(@Param("keyword") String keyword);
//    @Query("SELECT u FROM UserEntity u Where u.userIdx = :userIdx")
    Optional<UserEntity> findByUserIdx(Long userIdx);
//    Optional<UserEntity> findByUserIdx2(Long userIdx);

//    UserEntity findByUserIdAndPassword(String userId, String password);

}
