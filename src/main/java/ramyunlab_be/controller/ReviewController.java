package ramyunlab_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jdk.jshell.Snippet.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ramyunlab_be.dto.*;
import ramyunlab_be.entity.ReportEntity;
import ramyunlab_be.entity.ReviewEntity;
import ramyunlab_be.service.ReviewService;
import ramyunlab_be.vo.StatusCode;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces="application/json; charset=utf8")
@Tag(name = "Review", description = "리뷰 관련 API")
public class ReviewController {

    final private ReviewService reviewService;
    private Integer rvRecommendCount = 0;
    private Integer rvReportCount = 0;


    @Autowired
    public ReviewController(final ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @Operation(summary = "리뷰 작성", description = "RequestBody 에 rate 필수로 입력(리뷰 내용, 사진은 nullable)해야 합니다. (토큰 필요)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "리뷰 추가 성공"),
        @ApiResponse(responseCode = "400")
    })
    @PostMapping("/review/{ramyunIdx}")
    public ResponseEntity<ResDTO> addReview(@RequestPart(required = false) MultipartFile file,
                                            @Valid @RequestPart ReviewDTO reviewDTO,
                                            @PathVariable Long ramyunIdx,
                                            @AuthenticationPrincipal String userIdx) throws Exception{

        ReviewEntity createdReview = reviewService.create( ramyunIdx, userIdx,  reviewDTO, file);


        ReviewDTO responseReviewDTO = ReviewDTO.builder()
            .reviewContent(createdReview.getReviewContent())
            .reviewPhotoUrl(createdReview.getReviewPhotoUrl())
            .rvCreatedAt(createdReview.getRvCreatedAt())
            .rate(createdReview.getRate())
//            .rate(createdReview.getRate().toString())
            .rvIdx(createdReview.getRvIdx())
            .rvRecommendCount(createdReview.getRvRecommendCount())
            .rvReportCount(createdReview.getRvReportCount())
            .userIdx(createdReview.getUser().getUserIdx())
            .ramyunIdx(createdReview.getRamyun().getRamyunIdx())
            .build();

        return ResponseEntity.ok().body(ResDTO
            .builder()
            .statusCode(StatusCode.OK)
            .data(responseReviewDTO)
            .message("리뷰 추가 성공")
            .build());
    }

    @Operation(summary = "리뷰 수정", description = "RequestBody 에 rate 필수로 입력, token 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "리뷰 수정 성공"),
        @ApiResponse(responseCode = "400")
    })
    @PatchMapping("/review/{ramyunIdx}/{rvIdx}")
    public ResponseEntity<ResDTO<Object>> updateReview(@RequestPart(required = false) MultipartFile file,
                                               @Valid @RequestPart ReviewDTO reviewDTO,
                                               @PathVariable Long rvIdx,
                                               @PathVariable Long ramyunIdx,
                                               @AuthenticationPrincipal String userIdx) throws Exception{
        ReviewEntity updatedReview = reviewService.update(ramyunIdx, rvIdx, userIdx, reviewDTO, file);
        return ResponseEntity.ok().body(ResDTO
            .builder()
            .statusCode(StatusCode.OK)
            .data(updatedReview)
            .message("리뷰 수정 성공")
            .build());
    }

    @Operation(summary = "리뷰 삭제", description = "토큰만 있으면 됩니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공"),
        @ApiResponse(responseCode = "400")
    })
    @DeleteMapping("/review/{rvIdx}")
    public ResponseEntity<ResDTO> deleteReview(@PathVariable Long rvIdx,
                                               @AuthenticationPrincipal String userIdx){
        reviewService.delete(rvIdx, userIdx);
        return ResponseEntity.ok().body(ResDTO
           .builder()
           .statusCode(StatusCode.OK)
           .message("리뷰 삭제 성공")
           .build());
    }

    @Operation(summary = "내가 쓴 리뷰 목록 불러오기", description = "내가 쓴 리뷰 목록을 불러온다(With. pagination).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 쓴 리뷰 목록 호출 성공"),
            @ApiResponse(responseCode = "400", description = "내가 쓴 리뷰 목록 호출 실패")
    })
    @GetMapping("/user/myReview")
    public ResponseEntity<ResDTO> getMyReviewList(@Parameter(name = "page", description = "현재 페이지 번호", in = ParameterIn.QUERY, example = "1")
                                                      @RequestParam(name = "page", required = false) Integer pageNo,
                                                  @AuthenticationPrincipal String userIdx) {
        if(pageNo == null) pageNo = 1;
        Page<ReviewDTO> myReviewList = reviewService.getMyReviewList(pageNo, userIdx);

        return ResponseEntity.ok().body(ResDTO.builder()
                .statusCode(StatusCode.OK)
                .message("리뷰 목록 호출 완료.")
                .data(myReviewList)
                .build());
    }

    @PostMapping("/complaint/{rvIdx}")
    public ResponseEntity<ResDTO> complaint(@PathVariable Long rvIdx,
                                            @AuthenticationPrincipal String userIdx,
                                            @RequestBody ReportDTO reportDTO){
        ReportEntity createdReport =  reviewService.complaint(rvIdx, userIdx, reportDTO);

        ReportDTO responseReportDTO = ReportDTO.builder()
            .reportReason(createdReport.getReportReason())
            .reportIdx(createdReport.getReportIdx())
            .reportCreatedAt(createdReport.getReportCreatedAt())
            .userIdx(createdReport.getUser().getUserIdx())
            .reviewIdx(createdReport.getReview().getRvIdx())
            .build();
        log.warn("userIdx : {}", responseReportDTO.getUserIdx());
        return ResponseEntity.ok().body(ResDTO.builder()
               .statusCode(StatusCode.OK)
                .data(responseReportDTO)
               .message("신고 성공")
               .build());
    }



    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResDTO<Object>> handleValidationException(ValidationException e) {
        return ResponseEntity
            .badRequest()
            .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message(e.getMessage()).build());
    }

}
