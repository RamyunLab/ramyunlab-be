package ramyunlab_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.dto.RamyunFilterDTO;
import ramyunlab_be.dto.ReviewDTO;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.entity.ReviewEntity;

@Repository
public interface MainRepository extends JpaRepository<RamyunEntity, Long>, RamyunCustomRepository {

  Page<RamyunDTO> getRamyunList (Pageable pageable, String sort, String direction, RamyunFilterDTO filter);

  RamyunDTO getRamyunInfo (Long id);
}