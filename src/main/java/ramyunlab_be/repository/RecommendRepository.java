package ramyunlab_be.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ramyunlab_be.entity.FavoriteEntity;
import ramyunlab_be.entity.RecommendEntity;
import ramyunlab_be.entity.ReviewEntity;

@Repository
public interface RecommendRepository extends JpaRepository<RecommendEntity, Long> {
    // @Query("SELECT r FROM RecommendEntity r WHERE r.userIdx = :userIdx");
    Page<RecommendEntity> findByUser_UserIdx(Pageable pageable, Long userIdx);

    @Query("SELECT rec FROM RecommendEntity rec WHERE rec.review.rvIdx = :rvIdx AND rec.user.userIdx = :userIdx")
    Optional<RecommendEntity> findRecommend (Long rvIdx, Long userIdx);

}
