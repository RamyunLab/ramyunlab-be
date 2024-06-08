package ramyunlab_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDTO {
    private Long rvIdx;
    private String reviewContent;

    @NotNull(message = "별점을 입력해주세요.")
    @Min(value = 1, message = "별점은 최소 1이어야 합니다.")
    @Max(value = 5, message = "별점은 최대 5이어야 합니다.")
    private Integer rate;

    private String reviewPhoto;
    private Timestamp rvCreatedAt;
    private Timestamp rvUpdatedAt;
    private Timestamp rvDeletedAt;
    private Long userIdx;
    private Long ramyunIdx;

    @Override
    public String toString() {
        return "ReviewDTO{" +
            "rvIdx=" + rvIdx +
            ", reviewContent='" + reviewContent + '\'' +
            ", rate=" + rate +
            ", reviewPhoto='" + reviewPhoto + '\'' +
            ", rvCreatedAt='" + rvCreatedAt + '\'' +
            ", rvUpdatedAt='" + rvUpdatedAt + '\'' +
            ", rvDeletedAt='" + rvDeletedAt + '\'' +
            '}';
    }
}
