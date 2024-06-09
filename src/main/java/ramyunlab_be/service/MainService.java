package ramyunlab_be.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.repository.MainRepository;
import ramyunlab_be.vo.Pagenation;

@Slf4j
@RequiredArgsConstructor
@Service
public class MainService {

  private final MainRepository mainRepository;

  public Page<RamyunDTO> getAllList(int pageNo, String sort){
    log.info("SORT 전송::: {}", sort);
    Sort sortInfo;
    // Sort 정보 생성
    switch (sort != null ? sort : "name"){
      case "name":
        sortInfo = Sort.by(Sort.Direction.ASC, "ramyunName");
        break;
      case "name_desc":
        sortInfo = Sort.by(Sort.Direction.DESC, "ramyunName");
        break;
      default:
        sortInfo = Sort.by(Sort.Direction.ASC, "ramyunName");
        break;
    }

    // 페이징 정보 설정
    Pageable pageable = PageRequest.of(pageNo - 1, Pagenation.PAGE_SIZE, sortInfo);
    log.info("pageable ::: {}", pageable.getSort());

    // 라면 데이터 조회
    Page<RamyunEntity> result = mainRepository.findAll(pageable);
    Page<RamyunDTO> list = result.map(ramyun -> RamyunDTO.builder()
                                                       .ramyunIdx(ramyun.getRamyunIdx())
                                                       .brandName(ramyun.getBrand().getBrand())
                                                       .ramyunImg(ramyun.getRamyunImg())
                                                       .ramyunName(ramyun.getRamyunName())
                                                       .ramyunKcal(ramyun.getRamyunKcal())
                                                       .gram(ramyun.getGram())
                                                       .cooking(ramyun.getCooking())
                                                       .isCup(ramyun.getIsCup())
                                                       .noodle(ramyun.getNoodle())
                                                       .ramyunNa(ramyun.getRamyunNa())
                                                       .scoville(ramyun.getScoville())
                                                       .build());
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
    return list;
  }
}
