package ramyunlab_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.sql.Timestamp;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "리뷰 DTO")
public class ReviewDTO {
    @Schema(description = "리뷰 인덱스", example = "1")
    private Long rvIdx;

    @Schema(description = "리뷰 내용", example = "맛있어요.", nullable = true)
    private String reviewContent;

    @Schema(description = "별점", example = "4")
    @NotNull(message = "별점을 입력해주세요.")
    @Min(value = 1, message = "별점은 최소 1이어야 합니다.")
    @Max(value = 5, message = "별점은 최대 5이어야 합니다.")
//    private String rate;
    private Integer rate;

    @Schema(description = "사진 url", example = "dsfafa.png", nullable = true)
    private String reviewPhotoUrl;

    @Schema(description = "리뷰 작성 일자", example = "2024-06-07 15:57:32")
    private Timestamp rvCreatedAt;

    @Schema(description = "리뷰 수정 일자", example = "2024-06-07 15:57:32", nullable = true)
    private Timestamp rvUpdatedAt;

    @Schema(description = "리뷰 삭제 일자(논리 삭제 시 변경)", example = "2024-06-07 15:57:32", nullable = true)
    private Timestamp rvDeletedAt;

    @Schema(description = "공감 수", example = "1")
    private Integer rvRecommendCount;

    @Schema(description = "신고 수", example = "1")
    private Integer rvReportCount;

    @Schema(description = "회원 인덱스", example = "1")
    private Long userIdx;
    
    @Schema(description = "회원 닉네임", example = "테스트계정")
    private String userNickname;

    @Schema(description = "라면 인덱스", example = "1")
    private Long ramyunIdx;

    @Schema(description = "댓글 추천 여부", example = "true")
    private Boolean isRecommended = false;

    @Override
    public String toString() {
        return "ReviewDTO{" +
            "rvIdx=" + rvIdx +
            ", reviewContent='" + reviewContent + '\'' +
            ", rate=" + rate +
            ", reviewPhotoUrl='" + reviewPhotoUrl + '\'' +
            ", rvCreatedAt='" + rvCreatedAt + '\'' +
            ", rvUpdatedAt='" + rvUpdatedAt + '\'' +
            ", rvDeletedAt='" + rvDeletedAt + '\'' +
            '}';
    }
}
