package ramyunlab_be.repository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ramyunlab_be.dto.ReviewDTO;
import ramyunlab_be.entity.ReviewEntity;

import java.util.Optional;


@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>, ReviewCustomRepository {
    Page<ReviewEntity> findByUser_UserIdx(Pageable pageable, Long userIdx);

    /* 라면별 리뷰 조회 */
//    @Query("SELECT rv FROM ReviewEntity rv WHERE rv.ramyun.ramyunIdx = :ramyunIdx AND rv.rvDeletedAt IS NULL ORDER BY rv.rvCreatedAt DESC")
    Page<ReviewDTO> findReviewByRamyunIdx (Long ramyunIdx, Long userIdx, Pageable pageable);

    /* 라면별 베스트 리뷰 조회  */
//    @Query("SELECT rv FROM ReviewEntity rv WHERE rv.ramyun.ramyunIdx = :ramyunIdx AND rv.rvRecommendCount >= 10 AND rv.rvDeletedAt IS NULL ORDER BY rv.rvRecommendCount DESC, rv.rvCreatedAt ASC")
    Optional<List<ReviewDTO>> findBestReviewByRamyunIdx (Long ramyunIdx, Long userIdx, Pageable pageable);

    /* 리뷰 추천 시 추천수 변경 */
    @Query("UPDATE ReviewEntity rv SET rv.rvRecommendCount = rv.rvRecommendCount + 1 WHERE rv.rvIdx = :rvIdx")
    int plusRecommendCount(Long rvIdx);

    /* 리뷰 추천 삭제 시 추천수 변경 */
    @Query("UPDATE ReviewEntity rv SET rv.rvRecommendCount = rv.rvRecommendCount - 1 WHERE rv.rvIdx = :rvIdx")
    int minusRecommendCount(Long rvIdx);

    /* 리뷰 공감수 조회 */
    @Query("SELECT rv.rvRecommendCount FROM ReviewEntity rv WHERE rv.rvIdx = :rvIdx")
    Integer getReviewRecommendCount(Long rvIdx);

    @Query("SELECT rv FROM ReviewEntity rv WHERE rv.rvIsReported = TRUE ORDER BY rv.rvIsReported DESC")
    Page<ReviewEntity> findReportedReviewByReviewIdx(Long reviewIdx, Pageable pageable);

    @Query("SELECT rv FROM ReviewEntity rv WHERE rv.rvIsReported IS NOT NULL AND rv.rvIdx = :rvIdx")
    Optional<ReviewEntity> checkReportedReviewByIdx(Long rvIdx);

    @Query(value = "SELECT rv_report_count FROM review WHERE rv_idx = :rvIdx", nativeQuery = true)
    Optional<Long> findRvReportCountByRvIdx(@Param("rvIdx") Long rvIdx);

    @Modifying
    @Transactional
    @Query(value = "UPDATE review SET rv_report_count = rv_report_count + 1 WHERE rv_idx = :rvIdx", nativeQuery = true)
    Integer incrementRvReportCount(@Param("rvIdx") Long rvIdx);

    @Modifying
    @Transactional
    @Query(value = "UPDATE review SET rv_is_reported = 1 WHERE rv_idx = :rvIdx", nativeQuery = true)
    Integer changeIsReported(Long rvIdx);

    @Modifying
    @Transactional
    @Query(value = "UPDATE review SET rv_report_count = 0 WHERE rv_idx = :rvIdx", nativeQuery = true)
    Integer resetReportedCount(Long rvIdx);

    @Modifying
    @Transactional
    @Query(value = "UPDATE review SET rv_is_reported = 0 WHERE rv_idx = :rvIdx", nativeQuery = true)
    Integer resetIsReported(Long rvIdx);
}
