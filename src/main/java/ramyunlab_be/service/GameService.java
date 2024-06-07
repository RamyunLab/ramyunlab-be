package ramyunlab_be.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ramyunlab_be.dto.GameDTO;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.repository.GameRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameService {

  private final GameRepository gameRepository;

  public List<GameDTO> getRandomWorldCupList (int round){
    log.info("service round: {}", round);
    List<RamyunEntity> result = gameRepository.findRandomListByRound(round);
    List<GameDTO> worldCupList = new ArrayList<>();
    log.info("result: {}", result);

    for (RamyunEntity ramyun : result) {
      worldCupList.add(GameDTO.builder()
                                  .r_idx(ramyun.getRamyunIdx())
                                  .r_img(ramyun.getRamyunImg())
                                  .r_name(ramyun.getRamyunName())
                                  .build());
    }
    return worldCupList;
  }


  public List<GameDTO> getRandomListByScoville(){
    List<RamyunEntity> result = gameRepository.findRandomListByScoville();
    List<GameDTO> scovilleList = new ArrayList<>();
    log.info("result: {}", result);

    for (RamyunEntity ramyun : result) {
      scovilleList.add(GameDTO.builder()
                              .r_idx(ramyun.getRamyunIdx())
                              .r_img(ramyun.getRamyunImg())
                              .r_name(ramyun.getRamyunName())
                              .r_scoville(ramyun.getScoville())
                              .build());
    }
    return scovilleList;
  }


}
