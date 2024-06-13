package ramyunlab_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.dto.RamyunFilterDTO;
import ramyunlab_be.entity.RamyunEntity;

public interface RamyunCustomRepository {
  /* 라면 리스트 조회 (+필터링) */
  Page<RamyunDTO> getRamyunList (Pageable pageable, String sort, String direction, RamyunFilterDTO filter);

  /* 라면 상세 조회 */
  RamyunDTO getRamyunInfo (Long ramyunIdx);
}
