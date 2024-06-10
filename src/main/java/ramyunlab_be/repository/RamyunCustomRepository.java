package ramyunlab_be.repository;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ramyunlab_be.dto.RamyunDTO;

public interface RamyunCustomRepository {
  Page<RamyunDTO> findAllListQuery (Pageable pageable, String sort, String direction);

  Page<RamyunDTO> findFilteredListQuery (Pageable pageable, String sort, String direction);
}
