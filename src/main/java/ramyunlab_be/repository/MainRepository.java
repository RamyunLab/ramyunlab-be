package ramyunlab_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.entity.RamyunEntity;

@Repository
public interface MainRepository extends JpaRepository<RamyunEntity, Long>, RamyunCustomRepository {

  Page<RamyunEntity> findAll (Pageable pageable);

//  @Query(value = "SELECT r.*, avg(COALESCE(rv.rv_rate, 0)) as avgRate, COALESCE(count(rv.rv_rate), 0) as reviewCount "
  ////                 + "FROM Ramyun r "
  ////                 + "LEFT OUTER JOIN Review rv ON r.r_idx = rv.r_idx "
  ////                 + "GROUP BY r.r_idx ", nativeQuery = true)
  ////  Page<RamyunList> getListPage (Pageable pageable, @Param("sort") String sort);

  Page<RamyunDTO> findAllListQuery(Pageable pageable, String sort, String direction);
}


