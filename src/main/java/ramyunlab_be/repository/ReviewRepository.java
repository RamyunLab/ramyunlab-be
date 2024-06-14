package ramyunlab_be.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ramyunlab_be.entity.ReviewEntity;

import java.util.Optional;


@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Page<ReviewEntity> findByUser_UserIdx(Pageable pageable, Long userIdx);

    /* 라면별 리뷰 조회 */
    @Query("SELECT rv FROM ReviewEntity rv WHERE rv.ramyun.ramyunIdx = :ramyunIdx AND rv.rvDeletedAt IS NULL ORDER BY rv.rvCreatedAt DESC")
    Page<ReviewEntity> findReviewByRamyunIdx(Long ramyunIdx, Pageable pageable);

    /* 라면별 베스트 리뷰 조회  */
    @Query("SELECT rv FROM ReviewEntity rv WHERE rv.ramyun.ramyunIdx = :ramyunIdx AND rv.rvRecommendCount >= 10 AND rv.rvDeletedAt IS NULL ORDER BY rv.rvRecommendCount DESC, rv.rvCreatedAt ASC")
    Optional<List<ReviewEntity>> findBestReviewByRamyunIdx (Long ramyunIdx, Pageable pageable);

    @Query("SELECT rv FROM ReviewEntity rv WHERE rv.rvIsReported IS NOT NULL ORDER BY rv.rvIsReported DESC")
    Page<ReviewEntity> findReportedReviewByReviewIdx(Long reviewIdx, Pageable pageable);

    @Query("SELECT rv FROM ReviewEntity rv WHERE rv.rvIsReported IS NOT NULL AND rv.rvIdx = :rvIdx")
    Optional<ReviewEntity> checkReportedReviewByIdx(Long rvIdx);


}
