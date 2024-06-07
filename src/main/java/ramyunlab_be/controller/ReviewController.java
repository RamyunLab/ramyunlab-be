package ramyunlab_be.controller;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.dto.ReviewDTO;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.entity.ReviewEntity;
import ramyunlab_be.service.ReviewService;
import ramyunlab_be.vo.StatusCode;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces="application/json; charset=utf8")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/review/{ramyunIdx}")
    public ResponseEntity<ResDTO> addReview(@RequestBody ReviewDTO reviewDTO,
                                            @PathVariable Long ramyunIdx,
                                            @AuthenticationPrincipal String userIdx){
        log.warn("userIdxxxxxx {}", userIdx);
        ReviewEntity createdReview = reviewService.create( ramyunIdx, userIdx, reviewDTO);
        ReviewDTO responseReviewDTO = ReviewDTO.builder()
            .reviewContent(createdReview.getReviewContent())
            .reviewPhoto(createdReview.getReviewPhoto())
            .rvCreatedAt(createdReview.getRvCreatedAt())
            .rate(createdReview.getRate())
            .rvIdx(createdReview.getRvIdx())
            .build();

        return ResponseEntity.ok().body(ResDTO
            .builder()
            .statusCode(StatusCode.OK)
            .data(responseReviewDTO)
            .message("리뷰 추가 성공")
            .build());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResDTO> handleValidationException(ValidationException e) {
        return ResponseEntity
            .badRequest()
            .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message(e.getMessage()).build());
    }

}
