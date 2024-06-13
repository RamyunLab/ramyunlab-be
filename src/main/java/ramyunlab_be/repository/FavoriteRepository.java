package ramyunlab_be.repository;

import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ramyunlab_be.entity.FavoriteEntity;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity,Long> {
    // @Query("SELECT f FROM FavoriteEntity f Where f.userIdx = :userIdx");
    Page<FavoriteEntity> findByUser_UserIdx(Pageable pageable, Long userIdx);

    // 찜 조회
    @Query("SELECT fv FROM FavoriteEntity fv WHERE fv.user.userIdx = :userIdx AND fv.ramyun.ramyunIdx = :ramyunIdx")
    Optional<FavoriteEntity> findLikedRamyun (@Param("userIdx") Long userIdx, @Param("ramyunIdx") Long ramyunIdx);
}
