package ramyunlab_be.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.dto.RamyunFilterDTO;
import ramyunlab_be.repository.MainRepository;
import ramyunlab_be.vo.Pagination;

@Slf4j
@RequiredArgsConstructor
@Service
public class MainService {

  private MainRepository mainRepository;

  @Autowired
  public MainService(MainRepository mainRepository){
    this.mainRepository = mainRepository;
  }

  /* 라면 데이터 목록 조회 */
  public Page<RamyunDTO> getRamyunList (int pageNo, String sort, String direction, RamyunFilterDTO filter, Long userIdx){
    // 페이징 정보 설정
    Pageable pageable = PageRequest.of(pageNo - 1, Pagination.PAGE_SIZE);

    // 라면 데이터 조회
    Page<RamyunDTO> result = mainRepository.getRamyunList(pageable, sort, direction, filter, userIdx);
//    Boolean isLike = false;
//    for(RamyunDTO ramyun : result.getContent()){
//      log.info("RAMYUN IDX {}",ramyun.getRamyunIdx());
//      Optional<FavoriteEntity> fav = favoriteRepository.findLikedRamyun(userIdx, ramyun.getRamyunIdx());
//      if(fav.isPresent()){
//        isLike = true;
//      }
//      ramyun.setIsLiked(isLike);
//    }
    return result;
  }

  /* 라면 상세 정보 + 평점 조회 */
  public RamyunDTO getRamyun (long ramyunIdx){
    try {
      RamyunDTO ramyun = mainRepository.getRamyunInfo(ramyunIdx);
      return ramyun;
    }catch(Exception e){
      throw new RuntimeException("데이터를 조회할 수 없습니다.");
    }
  }
}
