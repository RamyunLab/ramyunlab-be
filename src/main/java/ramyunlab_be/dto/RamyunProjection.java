package ramyunlab_be.dto;

import java.sql.Timestamp;

public interface RamyunProjection {
    Long getRamyunIdx();
    String getRamyunName();
    String getRamyunImg();
    Integer getRamyunKcal();
    String getNoodle();
    Boolean getIsCup();
    Boolean getCooking();
    Integer getRamyunNa();
    Integer getGram();
    Integer getScoville();
    Timestamp getRamyunDeletedAt();
    String getBrandName();
}
