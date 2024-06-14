package ramyunlab_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ramyunlab_be.entity.ReportEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

}
