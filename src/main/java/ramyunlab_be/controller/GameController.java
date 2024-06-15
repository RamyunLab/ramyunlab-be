package ramyunlab_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ValidationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ramyunlab_be.dto.GameDTO;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.service.GameService;
import ramyunlab_be.vo.StatusCode;

@RestController
@RequiredArgsConstructor
@Tag(name = "Game", description = "게임과 관련한 API 명세입니다.")
@RequestMapping("/game")
public class GameController {

  private final GameService gameService;

  @Operation(summary = "라면 랜덤 조회", description = "이상형 월드컵 라면 랜덤 조회 (16/32/64강 중 라운드 선택)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "라면 목록 조회 성공"),
      @ApiResponse(responseCode = "500", description = "라면 목록 조회 실패")
  })
  @GetMapping("/worldCup/{round}")
  public ResponseEntity<ResDTO<Object>> getRandomWorldCupList (@Parameter(description = "라운드 수(16/32/64 중에서 전송)")
                                                           @PathVariable int round){
    List<GameDTO> result = gameService.getRandomWorldCupList(round);
    return ResponseEntity.ok().body(ResDTO.builder()
                                          .statusCode(StatusCode.OK)
                                          .message("목록 조회 성공")
                                          .data(result)
                                          .build());
  }

  @Operation(summary = "라면 랜덤 조회(Up & Down)", description = "스코빌 지수 존재하는 라면 조회(11개)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "라면 목록 조회 성공"),
      @ApiResponse(responseCode = "500", description = "라면 목록 조회 실패")
  })
  @GetMapping("/updown")
  public ResponseEntity<ResDTO> getUpDownList (){
    List<GameDTO> result =  gameService.getRandomListByScoville();
    return ResponseEntity.ok().body(ResDTO.builder()
                                          .statusCode(StatusCode.OK)
                                          .message("추가")
                                          .data(result)
                                          .build());

  }

  @Operation(summary = "게임 결과 조회", description = "결과 라면 조회하기")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "라면 조회 성공"),
      @ApiResponse(responseCode = "500", description = "라면 조회 실패")
  })
  @GetMapping("/result/{ramyunIdx}")
  public ResponseEntity<ResDTO<Object>> getResult (@Parameter(name = "라면 인덱스", in = ParameterIn.PATH)
                                                     @PathVariable Long ramyunIdx){
    GameDTO result = gameService.getResult(ramyunIdx);
    return ResponseEntity.ok().body(ResDTO.builder()
                                          .statusCode(StatusCode.OK)
                                          .message("라면 조회 성공")
                                          .data(result)
                                          .build());
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ResDTO<Object>> handleValidationException (ValidationException e) {
    return ResponseEntity
        .badRequest()
        .body(ResDTO.builder()
                    .statusCode(StatusCode.BAD_REQUEST)
                    .message(e.getMessage())
                    .build());
  }

}
