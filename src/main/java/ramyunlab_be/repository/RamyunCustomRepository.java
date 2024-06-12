package ramyunlab_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.dto.RamyunFilterDTO;
import ramyunlab_be.entity.RamyunEntity;

public interface RamyunCustomRepository {
  Page<RamyunDTO> getRamyunList (Pageable pageable, String sort, String direction, RamyunFilterDTO filter);
  RamyunDTO getRamyunInfo (Long id);
}
