package ramyunlab_be.dto.ReviewProjection;

import java.sql.Timestamp;

public interface ReviewProjection {
    Long getReviewIdx();
    String getReviewContent();
    Integer getRate();
    String getReviewPhotoUrl();
    Timestamp getRvCreatedAt();
    Timestamp getRvUpdatedAt();
    Timestamp getRvDeletedAt();
    Integer getRvRecommendCount();
    Boolean getRvIsReported();
    Integer getRvReportCount();
    Long getUserIdx();
    Long getRamyunIdx();
}
