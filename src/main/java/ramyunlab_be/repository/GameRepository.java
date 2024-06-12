package ramyunlab_be.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ramyunlab_be.entity.RamyunEntity;

public interface GameRepository extends JpaRepository<RamyunEntity, Long> {

  // round 수를 받아 해당 수만큼 라면 데이터 랜덤 조회
  @Query(value = "SELECT * FROM ramyun ORDER BY RAND() LIMIT :round", nativeQuery = true)
  List<RamyunEntity> findRandomListByRound (@Param("round") int round);

  @Query(value = "SELECT r.* FROM ( SELECT ramyun.*, "
                 + "ROW_NUMBER() OVER (PARTITION BY r_scoville ORDER BY r_scoville) AS rnk "
                 + "FROM ramyun) r "
                 + "WHERE rnk = 1 AND r_scoville IS NOT NULL "
                 + "ORDER BY RAND() "
                 + "LIMIT 11", nativeQuery = true)
  List<RamyunEntity> findRandomListByScoville();

}
