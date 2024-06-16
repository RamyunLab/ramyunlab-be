package ramyunlab_be.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ramyunlab_be.dto.GameDTO;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.repository.GameRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameService {

  private GameRepository gameRepository;

  @Autowired
  public GameService(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  public List<GameDTO> getRandomWorldCupList (int round){
    log.info("service round: {}", round);
    List<RamyunEntity> result = gameRepository.findRandomListByRound(round);
    List<GameDTO> worldCupList = new ArrayList<>();

    for (RamyunEntity ramyun : result) {
      worldCupList.add(convertToDTO(ramyun));
    }
    return worldCupList;
  }

  public List<GameDTO> getRandomListByScoville(){
    List<RamyunEntity> result = gameRepository.findRandomListByScoville();
    List<GameDTO> scovilleList = new ArrayList<>();
    for (RamyunEntity ramyun : result) {
      scovilleList.add(convertToDTO(ramyun));
    }
    return scovilleList;
  }

  public GameDTO getResult (Long ramyunIdx){
    RamyunEntity result = gameRepository.findById(ramyunIdx)
                                        .orElseThrow(() -> new RuntimeException("해당 라면이 존재하지 않습니다."));
    return convertToDTO(result);
  }

  public Long getRandomRamyunIdx(){
    List<RamyunEntity> result =  gameRepository.findRandomListByRound(1);
    return result.get(0).getRamyunIdx();
  }

  public GameDTO convertToDTO(RamyunEntity entity){
    return GameDTO.builder()
                         .r_idx(entity.getRamyunIdx())
                         .r_img(entity.getRamyunImg())
                         .r_name(entity.getRamyunName())
                         .r_scoville(entity.getScoville())
                         .build();
  }
}
