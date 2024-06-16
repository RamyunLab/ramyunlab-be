package ramyunlab_be.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import ramyunlab_be.dto.ReviewDTO;
import ramyunlab_be.entity.ReviewEntity;

public interface ReviewCustomRepository {
  /* 라면별 리뷰 조회 */
  Page<ReviewDTO> findReviewByRamyunIdx (Long ramyunIdx, Long userIdx, Pageable pageable);

  /* 라면별 베스트 리뷰 조회  */
//  @Query("SELECT rv FROM ReviewEntity rv WHERE rv.ramyun.ramyunIdx = :ramyunIdx AND rv.rvRecommendCount >= 10 AND rv.rvDeletedAt IS NULL ORDER BY rv.rvRecommendCount DESC, rv.rvCreatedAt ASC")
  Optional<List<ReviewDTO>> findBestReviewByRamyunIdx (Long ramyunIdx, Long userIdx, Pageable pageable);
}
