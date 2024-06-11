package ramyunlab_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ramyunlab_be.entity.RamyunEntity;

import java.util.List;

@Repository
public interface RamyunRepository extends JpaRepository<RamyunEntity, Long> {
    @Override
    List<RamyunEntity> findAll();

//    Page<RamyunEntity> findByAddress(String address, Pageable pageable);
}
