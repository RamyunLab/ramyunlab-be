package ramyunlab_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ReviewService reviewService;

    @Operation(summary = "리뷰 작성", description = "RequestBody 에 rate 필수로 입력(리뷰 내용, 사진은 nullable)해야 합니다. (토큰 필요)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "리뷰 추가 성공"),
        @ApiResponse(responseCode = "400")
    })
    @PostMapping("/review/{ramyunIdx}")
    public ResponseEntity<ResDTO> addReview(@Valid @RequestBody ReviewDTO reviewDTO,
                                            @ModelAttribute ReviewUploadPhotoDTO reviewUploadPhotoDTO,
                                            @PathVariable Long ramyunIdx,
                                            @AuthenticationPrincipal String userIdx){
        ReviewEntity createdReview = reviewService.create( ramyunIdx, userIdx, reviewDTO, reviewUploadPhotoDTO);


        ReviewDTO responseReviewDTO = ReviewDTO.builder()
            .reviewContent(createdReview.getReviewContent())
            .reviewPhotoUrls(createdReview.getReviewPhotoUrls())
            .rvCreatedAt(createdReview.getRvCreatedAt())
            .rate(createdReview.getRate())
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

    @Operation(summary = "리뷰 수정", description = "리뷰 수정")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "리뷰 수정 성공"),
        @ApiResponse(responseCode = "400")
    })
    @PatchMapping("/review/{rvIdx}")
    public ResponseEntity<ResDTO> updateReview(@Valid @RequestBody ReviewDTO reviewDTO,
                                               @PathVariable Long rvIdx,
                                               @AuthenticationPrincipal String userIdx){

        ReviewEntity updatedReview = reviewService.update(rvIdx, userIdx, reviewDTO);

        return ResponseEntity.ok().body(ResDTO
            .builder()
            .statusCode(StatusCode.OK)
            .data(updatedReview)
            .message("리뷰 수정 성공")
            .build());
    }

//    @PostMapping("/review/photo")
//    public ResponseEntity<ResDTO> uploadPhoto (ReviewUploadPhotoDTO reviewUploadPhotoDTO){
//
//    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResDTO> handleValidationException(ValidationException e) {
        return ResponseEntity
            .badRequest()
            .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message(e.getMessage()).build());
    }

}
