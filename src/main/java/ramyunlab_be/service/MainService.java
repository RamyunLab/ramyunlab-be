package ramyunlab_be.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.repository.MainRepository;
import ramyunlab_be.vo.Pagenation;

@Slf4j
@RequiredArgsConstructor
@Service
public class MainService {

  private final MainRepository mainRepository;

  public Page<RamyunDTO> getAllList(int pageNo, String sort, String direction){

    // 페이징 정보 설정
    Pageable pageable = PageRequest.of(pageNo - 1, Pagenation.PAGE_SIZE);
    log.info("pageable ::: {}", pageable.getSort());

    // 라면 데이터 조회
    Page<RamyunDTO> result = mainRepository.findAllListQuery(pageable, sort, direction);
    log.info("데이터 조회::: {}", result.getTotalElements());

    /* 총 페이지 개수 */
    int totalPages = result.getTotalPages();
    /* 총 요소 개수 */
    Long totalElements = result.getTotalElements();
    /* 첫번째 페이지 여부 */
    boolean isFirst = result.isFirst();
    /* 마지막 페이지 여부 */
    boolean isLast = result.isLast();
    /* 현재 페이지 */
    int page = result.getNumber();

    log.info("isFirst??  {}", result.isFirst());
    log.info("isLast??  {}", result.isLast());

    if(result.getTotalElements() == 0){
      throw new RuntimeException("조회 결과가 없습니다.");
    }

    return result;
  }

//  public Page<RamyunDTO> getFilteredList(){}
}
