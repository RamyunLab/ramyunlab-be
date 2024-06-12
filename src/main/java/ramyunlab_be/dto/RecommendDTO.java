package ramyunlab_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "공감 DTO")
public class RecommendDTO {

    @Schema(description = "공감 인덱스", example = "1")
    private Long recommendIdx;

    @Schema(description = "공감 작성 일자", example = "2024-06-07 15:57:32")
    private Timestamp recCreatedAt;

    @Schema(description = "회원 인덱스", example = "1")
    private Long userIdx;

    @Schema(description = "리뷰 인덱스", example = "1")
    private Long reviewIdx;

    @Override
    public String toString() {
        return "RecommendDTO{" +
            "recommendIdx=" + recommendIdx +
            ", recCreatedAt=" + recCreatedAt +
            ", userIdx=" + userIdx +
            ", reviewIdx=" + reviewIdx +
            '}';
    }
}
