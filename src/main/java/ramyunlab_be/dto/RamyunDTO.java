package ramyunlab_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RamyunDTO", description = "라면 전체 정보")
public class RamyunDTO {

  private Long ramyunIdx;
  private String ramyunName;
  private String ramyunImg;
  private String brandName;
  private Boolean noodle;
  private Integer ramyunKcal;
  private Boolean isCup;
  private Boolean cooking;
  private Integer gram;
  private Integer ramyunNa;
  private Integer scoville;
  private double avrRate;

  @Override
  public String toString () {
    return "RamyunDTO{" +
           "ramyunIdx=" + ramyunIdx +
           ", ramyunName='" + ramyunName + '\'' +
           ", ramyunImg='" + ramyunImg + '\'' +
           ", brandName=" + brandName +
           ", noodle=" + noodle +
           ", ramyunKcal=" + ramyunKcal +
           ", isCup=" + isCup +
           ", cooking=" + cooking +
           ", gram=" + gram +
           ", ramyunNa=" + ramyunNa +
           ", scoville=" + scoville +
           '}';
  }
}
