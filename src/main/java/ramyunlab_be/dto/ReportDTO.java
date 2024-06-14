package ramyunlab_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "신고된 리뷰 정보 DTO")
public class ReportDTO {
    @Schema(description = "신고 인덱스", example = "1")
    private Long reportIdx;

    @Schema(description = "신고사유", example = "부적절한 광고")
    private String reportReason;

    @Schema(description = "신고 날짜", example = "2021-09-01 12:00")
    private Timestamp reportCreatedAt;

    @Schema(description = "신고자 인덱스", example = "1")
    private Long userIdx;

    @Schema(description = "신고된 리뷰 인덱스", example = "1")
    private Long reviewIdx;
}
