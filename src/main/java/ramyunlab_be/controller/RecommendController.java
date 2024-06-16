package ramyunlab_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ramyunlab_be.dto.RecReviewDTO;
import ramyunlab_be.dto.RecommendDTO;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.entity.RecommendEntity;
import ramyunlab_be.service.RecommendService;
import ramyunlab_be.service.ReviewService;
import ramyunlab_be.vo.StatusCode;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces="application/json; charset=utf8")
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@CrossOrigin(origins = "*")
@Tag(name = "Recommend", description = "공감 관련 API")
public class RecommendController {

    private final RecommendService recommendService;
    private final ReviewService reviewService;

    @Autowired
    public RecommendController (final RecommendService recommendService, ReviewService reviewService){
        this.recommendService = recommendService;
        this.reviewService = reviewService;
    }

    @Operation(summary = "공감 추가", description = "토큰만 있으면 됩니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "공감 추가 성공"),
        @ApiResponse(responseCode = "400")
    })
    @PostMapping("/recReview/{rvIdx}")
    public ResponseEntity<ResDTO> recommend(@PathVariable Long rvIdx,
                                            @AuthenticationPrincipal String userIdx){
        RecommendEntity addRecommend = recommendService.create(rvIdx, userIdx);

        RecommendDTO responseRecommendDTO = RecommendDTO.builder()
            .recommendIdx(addRecommend.getRecommendIdx())
            .userIdx(addRecommend.getUser().getUserIdx())
            .recCreatedAt(addRecommend.getRecCreatedAt())
            .reviewIdx(addRecommend.getReview().getRvIdx())
            .build();

        return ResponseEntity.ok().body(ResDTO
            .builder()
            .statusCode(StatusCode.OK)
            .data(responseRecommendDTO)
            .message("공감 추가 성공")
            .build());
    }

    @DeleteMapping("/recReview/{recommendIdx}")
    public ResponseEntity<ResDTO> deleteRecommend(@PathVariable Long recommendIdx,
                                                   @AuthenticationPrincipal String userIdx){
        recommendService.delete(recommendIdx, userIdx);
        return ResponseEntity.ok().body(ResDTO
           .builder()
           .statusCode(StatusCode.OK)
           .message("공감 삭제 성공")
           .build());
    }

    @GetMapping("/recReview")
    public ResponseEntity<ResDTO> getRecReview(@RequestParam(name = "page", required = false) Integer pageNo,
                                               @AuthenticationPrincipal String userIdx) {
        if(pageNo == null) pageNo = 1;
        Page<RecReviewDTO> Lists = recommendService.getMyRecReview(pageNo,userIdx);
        return ResponseEntity.ok().body(ResDTO.builder()
                .statusCode(StatusCode.OK)
                .message("목록 호출 완료")
                .data(Lists)
                .build());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResDTO> handleValidationException(ValidationException e) {
        return ResponseEntity
            .badRequest()
            .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message(e.getMessage()).build());
    }
}
