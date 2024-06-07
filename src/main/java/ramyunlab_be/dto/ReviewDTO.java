package ramyunlab_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
