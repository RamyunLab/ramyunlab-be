package ramyunlab_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetReviewDTO {
    private Long reviewIdx;
    private String reviewContent;
    private Integer rate;
    private String reviewPhotoUrl;
    private Timestamp rvCreatedAt;
    private Timestamp rvUpdatedAt;
    private Timestamp rvDeletedAt;
    private Integer rvRecommendCount;
    private Boolean rvIsReported;
    private Integer rvReportCount;
    private Long userIdx;
    private Long ramyunIdx;
}
