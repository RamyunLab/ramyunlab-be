package ramyunlab_be.dto;

import ramyunlab_be.entity.ReviewEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public interface UserProjection {
    Long getUserIdx();
    String getUserId();
    String getNickname();
    Boolean getIsAdmin();
    Timestamp getUserDeletedAt();
}


