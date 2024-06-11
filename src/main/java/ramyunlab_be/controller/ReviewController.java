package ramyunlab_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.dto.ReviewDTO;
import ramyunlab_be.dto.ReviewUploadPhotoDTO;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.entity.ReviewEntity;
import ramyunlab_be.service.ReviewService;
import ramyunlab_be.vo.StatusCode;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces="application/json; charset=utf8")
@Tag(name = "Review", description = "리뷰 관련 API")
public class ReviewController {

    final private ReviewService reviewService;

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
        ReviewEntity createdReview = reviewService.create( ramyunIdx, userIdx, reviewDTO, file);


        ReviewDTO responseReviewDTO = ReviewDTO.builder()
            .reviewContent(createdReview.getReviewContent())
            .reviewPhotoUrl(createdReview.getReviewPhotoUrl())
            .rvCreatedAt(createdReview.getRvCreatedAt())
            .rate(createdReview.getRate().toString())
            .rvIdx(createdReview.getRvIdx())
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
    public ResponseEntity<ResDTO> updateReview(@RequestPart(required = false) MultipartFile file,
                                               @Valid @RequestPart ReviewDTO reviewDTO,
                                               @PathVariable Long rvIdx,
                                               @PathVariable Long ramyunIdx,
                                               @AuthenticationPrincipal String userIdx) throws Exception{
        log.warn("controller 1 {}, {}, {}, {}, {}", ramyunIdx, rvIdx, userIdx, reviewDTO, file);
        ReviewEntity updatedReview = reviewService.update(ramyunIdx, rvIdx, userIdx, reviewDTO, file);
        log.warn("controller 2 {}, {}, {}, {}, {}", ramyunIdx, rvIdx, userIdx, reviewDTO, file);
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


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResDTO> handleValidationException(ValidationException e) {
        return ResponseEntity
            .badRequest()
            .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message(e.getMessage()).build());
    }

}
