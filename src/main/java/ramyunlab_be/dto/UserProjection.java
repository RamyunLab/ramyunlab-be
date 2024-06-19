package ramyunlab_be.dto;

import java.sql.Timestamp;
import java.util.List;

public interface UserProjection {
    Long getUserIdx();

    String getUserId();

    String getNickname();

    Boolean getIsAdmin();

    Timestamp getUserDeletedAt();

    String getReviews();
}


