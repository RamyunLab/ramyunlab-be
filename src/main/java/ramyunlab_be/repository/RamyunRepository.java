package ramyunlab_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ramyunlab_be.entity.RamyunEntity;

@Repository
public interface RamyunRepository  extends JpaRepository<RamyunEntity, Long> {
}
