package ramyunlab_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ramyunlab_be.entity.RecommendEntity;

@Repository
public interface RecommendRepository extends JpaRepository<RecommendEntity, Long> {

}
