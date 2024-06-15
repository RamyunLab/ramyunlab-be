package ramyunlab_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ramyunlab_be.entity.ReportEntity;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    @Query("SELECT rp FROM ReportEntity rp WHERE rp.review.rvIdx = :rvIdx AND rp.user.userIdx = :userIdx")
    ReportEntity findReportedReviewByRvIdx(@Param("rvIdx") Long rvIdx, @Param("userIdx") Long userIdx);

}
