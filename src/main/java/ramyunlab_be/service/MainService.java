package ramyunlab_be.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.dto.RamyunFilterDTO;
import ramyunlab_be.repository.MainRepository;
import ramyunlab_be.vo.Pagenation;

@Slf4j
@RequiredArgsConstructor
@Service
public class MainService {

  private final MainRepository mainRepository;

  public Page<RamyunDTO> getRamyunList (int pageNo, String sort, String direction, RamyunFilterDTO filter){
    // 페이징 정보 설정
    Pageable pageable = PageRequest.of(pageNo - 1, Pagenation.PAGE_SIZE);

    // 라면 데이터 조회
    Page<RamyunDTO> result = mainRepository.getRamyunList(pageable, sort, direction, filter);

    return result;
  }

}
