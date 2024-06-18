package ramyunlab_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ramyunlab_be.dto.RamyunProjection;
import ramyunlab_be.entity.RamyunEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface RamyunRepository extends JpaRepository<RamyunEntity, Long> {
    @Query("SELECT r.ramyunIdx as ramyunIdx, r.ramyunName as ramyunName, r.ramyunImg as ramyunImg, r.ramyunKcal as ramyunKcal, r.noodle as noodle, r.isCup as isCup, r.cooking as cooking, r.ramyunNa as ramyunNa, r.gram as gram, r.scoville as scoville, r.ramyunDeletedAt as ramyunDeletedAt, b.brandName as brandName FROM RamyunEntity r JOIN r.brand b")
    Page<RamyunProjection> findAllWithBrand(Pageable pageable);

    @Query("SELECT r.ramyunName as ramyunName, r.ramyunImg as ramyunImg, r.ramyunKcal as ramyunKcal, r.noodle as noodle, r.isCup as isCup, r.cooking as cooking, r.ramyunNa as ramyunNa, r.gram as gram, r.scoville as scoville, r.ramyunDeletedAt as ramyunDeletedAt, b.brandName as brandName FROM RamyunEntity r JOIN r.brand b WHERE r.ramyunIdx = :ramyunIdx")
    Optional<RamyunProjection> findByRamyunIdx(@Param("ramyunIdx") Long ramyunIdx);

}
