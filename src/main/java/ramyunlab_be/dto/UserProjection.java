package ramyunlab_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.List;

public interface UserProjection {
    Long getUserIdx();

    String getUserId();

    String getNickname();

    Boolean getIsAdmin();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd 'T' HH:mm:ss", timezone = "Asia/Seoul")
    Timestamp getUserDeletedAt();

    String getReviews();

    Boolean getRvIsReported();

    Long getRvIdx();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd 'T' HH:mm:ss", timezone = "Asia/Seoul")
    Timestamp getRvCreatedAt();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd 'T' HH:mm:ss", timezone = "Asia/Seoul")
    Timestamp getRvDeletedAt();
}


