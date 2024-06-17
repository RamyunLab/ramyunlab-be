package ramyunlab_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.dto.RamyunDetailDTO;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.dto.RamyunFilterDTO;
import ramyunlab_be.dto.ReviewDTO;
import ramyunlab_be.globalExceptionHandler.Util;
import ramyunlab_be.security.TokenProvider;
import ramyunlab_be.service.FavoriteService;
import ramyunlab_be.service.GameService;
import ramyunlab_be.service.MainService;
import ramyunlab_be.service.RecommendService;
import ramyunlab_be.service.ReviewService;
import ramyunlab_be.vo.StatusCode;

@Slf4j
@Tag(name = "Main", description = "메인 페이지 라면 데이터 관련 API 명세")
@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

  private MainService mainService;
  private ReviewService reviewService;
  private FavoriteService favoriteService;
  private GameService gameService;

  @Autowired
  public MainController (MainService mainService, ReviewService reviewService, FavoriteService favoriteService, GameService gameService){
    this.mainService = mainService;
    this.gameService = gameService;
    this.reviewService = reviewService;
    this.favoriteService = favoriteService;
  }

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private TokenProvider tokenProvider;

  @Operation(summary = "모든 라면 정보 조회", description = "메인페이지 전체 라면 정보를 조회함 (+페이지네이션) ")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", useReturnTypeSchema = true, description = "데이터 조회 성공"),
      @ApiResponse(responseCode = "400", useReturnTypeSchema = true, description = "데이터 조회 실패")
  })
  @GetMapping
  public ResponseEntity<ResDTO<Object>> getAllList (@Parameter(name="page", description = "현재 페이지 번호", in = ParameterIn.QUERY, example = "1")
                                                      @RequestParam(name="page", required = false) Integer page,
                                                    @Parameter(name="sort", description = "정렬 기준", in = ParameterIn.QUERY, example = "name")
                                                      @RequestParam(value="sort", required = false) String sort,
                                                    @Parameter(name="direction", description = "정렬 순서", in = ParameterIn.QUERY, example = "desc")
                                                      @RequestParam(value = "direction", required = false) String direction,
                                                    @Parameter(name="searchDTO", description = "필터링 조건", in = ParameterIn.QUERY)
                                                      RamyunFilterDTO filter,
                                                      @AuthenticationPrincipal String userIdx){
    Long user = null;
    if(userIdx != null && !userIdx.equals("anonymousUser")) {
      user = Long.parseLong(userIdx);
    }

    // 라면 목록 조회
    if(page == null) page = 1;
    Page<RamyunDTO> result = mainService.getRamyunList(page, sort, direction, filter, user);

    return ResponseEntity.ok().body(ResDTO.builder()
                                          .statusCode(StatusCode.OK)
                                          .message("데이터 조회 성공")
                                          .data(result)
                                          .build());
  }

    @Operation(summary = "필터링된 라면 정보 조회",
               description = "조건을 걸어 필터링한 라면 정보를 조회함 (+페이지네이션)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", useReturnTypeSchema = true, description = "데이터 조회 성공"),
        @ApiResponse(responseCode = "400", useReturnTypeSchema = true, description = "데이터 조회 실패")
    })
    @GetMapping("/search")
    public ResponseEntity<ResDTO<Object>> getFilteredList (@Parameter(name="page", description = "현재 페이지 번호", in = ParameterIn.QUERY, example = "1")
                                                            @RequestParam(name="page", required = false) Integer page,
                                                          @Parameter(name="sort", description = "정렬 기준", in = ParameterIn.QUERY, example = "name")
                                                            @RequestParam(value="sort", required = false) String sort,
                                                          @Parameter(name="direction", description = "정렬 순서", in = ParameterIn.QUERY, example = "desc")
                                                            @RequestParam(value = "direction", required = false) String direction,
                                                          @Parameter(name="searchDTO", description = "필터링 조건", in = ParameterIn.QUERY)
                                                            RamyunFilterDTO filter,
                                                          @AuthenticationPrincipal String userIdx){
      Long user = null;
      if(userIdx != null && !userIdx.equals("anonymousUser")) {
        user = Long.parseLong(userIdx);
      }

      if(page == null) page = 1;

      Page<RamyunDTO> result = mainService.getRamyunList(page, sort, direction, filter, user);
      return ResponseEntity.ok().body(ResDTO.builder().statusCode(StatusCode.OK).message("데이터 조회 성공").data(result).build());
    }

  @Operation(summary = "라면 상세페이지 정보 조회",
             description = "라면 정보 및 라면에 대한 리뷰 첫 페이지를 조회함")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", useReturnTypeSchema = true, description = "데이터 조회 성공")
  })
  @GetMapping("/ramyun/{ramyunIdx}")
  public ResponseEntity<ResDTO<Object>> getRamyunInfo (@Parameter(name="ramyunIdx", description = "라면 인덱스", example = "1")
                                                         @PathVariable Long ramyunIdx,
                                                       @Parameter(name="page", description = "리뷰 페이지", example="1")
                                                       @RequestParam(value = "page", required = false) Integer page,
                                                       @Parameter(name="reviewNo", description = "리뷰 번호", example = "20")
                                                         @RequestParam(value="reviewNo", required = false) Long reviewNo,
                                                       @AuthenticationPrincipal String userIdx){
   // 로그인 유저 판별
    Long user = null;
    boolean isLiked = false;
    if(userIdx != null && !userIdx.equals("anonymousUser")) {
      user = Long.parseLong(userIdx);
      isLiked = favoriteService.isLiked(user, ramyunIdx);
    }

    // 라면 + 평점 조회
    RamyunDTO ramyun = mainService.getRamyun(ramyunIdx);
    log.info("ramyun {}", ramyun.toString());

    if(page == null){
      page = 1;
    }
    log.info("page {}", page);

    // 리뷰 조회
    Page<ReviewDTO> reviews = reviewService.getReviewByRamyun(ramyunIdx, user, page);
    log.info("review {}", reviews.toString());

    // 베스트 리뷰 조회
    List<ReviewDTO> bestReview = reviewService.getBestReviewByRamyun(ramyunIdx, user);

    return ResponseEntity.ok().body(ResDTO.builder()
                                          .statusCode(StatusCode.OK)
                                          .message("데이터 조회 성공")
                                          .data(RamyunDetailDTO.builder().ramyun(ramyun)
                                                               .bestReview(bestReview)
                                                               .review(reviews)
                                                               .isLiked(isLiked)
                                                               .reviewNo(reviewNo).build())
                                          .build());
  }

  @Operation(summary = "라면 리뷰 조회",
             description = "특정 라면에 대한 리뷰 정보를 조회함 (리뷰 페이지 버튼 클릭 시 해당 API로 연결 필요)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", useReturnTypeSchema = true, description = "데이터 조회 성공")
  })
  @GetMapping("/ramyun/{ramyunIdx}/review")
  public ResponseEntity<ResDTO<Object>> getRamyunReview (@Parameter(name="ramyunIdx", description = "라면 인덱스", example = "1")
                                                         @PathVariable Long ramyunIdx,
                                                         @Parameter(name="page", description = "페이지", example = "1")
                                                         @RequestParam(value = "page", required = false) Integer page,
                                                         @Parameter(name="reviewNo", description = "리뷰 번호", example = "20")
                                                         @RequestParam(value="reviewNo", required = false) Long reviewNo,
                                                         @AuthenticationPrincipal String userIdx){
    // 리뷰 조회
    Long user = null;
    if(page == null) page = 1;
    if(userIdx != null && !userIdx.equals("anonymousUser")) {
      user = Long.parseLong(userIdx);
    }

    Page<ReviewDTO> result = reviewService.getReviewByRamyun(ramyunIdx, user, page);

    return ResponseEntity.ok().body(ResDTO.builder().statusCode(StatusCode.OK)
                                          .message("리뷰 조회 성공")
                                          .data(RamyunDetailDTO.builder().review(result).build()).build());
  }

  @Operation(summary = "내 리뷰 조회", description = "마이페이지에서 리뷰 클릭 시 상세페이지에서 조회, 라면인덱스(ramyunIdx), 리뷰인덱스(reviewNo) 필요")
  @GetMapping("/ramyun/{ramyunIdx}/my")
  public ResponseEntity<ResDTO<Object>> getReviewPage(@PathVariable Long ramyunIdx,
                                                      @RequestParam(required = true) Long reviewNo,
                                                      @AuthenticationPrincipal String userIdx){
    Integer page = reviewService.goMyReview(ramyunIdx, reviewNo);
    log.info("리뷰 조회 시 페이지 {}", page);
    return getRamyunInfo(ramyunIdx, page, reviewNo, userIdx);
  }

  @Operation(summary = "라면 상세페이지 랜덤 조회", description = "라면 상세페이지 랜덤 조회, 토큰 필요함")
    @GetMapping("/random")
    public ResponseEntity<ResDTO<Object>> getRandomRamyunIdx(@AuthenticationPrincipal String userIdx){
      Long result = gameService.getRandomRamyunIdx();
      return getRamyunInfo(result, null, null, userIdx);
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
