package ramyunlab_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd 'T' HH:mm:ss", timezone = "Asia/Seoul")
    Timestamp getRamyunDeletedAt();
    String getBrandName();
}
