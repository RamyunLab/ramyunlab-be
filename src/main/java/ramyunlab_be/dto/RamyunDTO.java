package ramyunlab_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ramyunlab_be.entity.BrandEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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
  private Double avgRate;
  private Long reviewCount;
  private Long brandIdx;

  @Override
  public String toString() {
    return "RamyunDTO{" +
        "ramyunIdx=" + ramyunIdx +
        ", ramyunName='" + ramyunName + '\'' +
        ", ramyunImg='" + ramyunImg + '\'' +
        ", brandName='" + brandName + '\'' +
        ", noodle=" + noodle +
        ", ramyunKcal=" + ramyunKcal +
        ", isCup=" + isCup +
        ", cooking=" + cooking +
        ", gram=" + gram +
        ", ramyunNa=" + ramyunNa +
        ", scoville=" + scoville +
        ", avgRate=" + avgRate +
        ", reviewCount=" + reviewCount +
        ", brandIdx=" + brandIdx +
        '}';
  }

  public BrandEntity getBrand() {
    return BrandEntity.builder()
       .brandIdx(brandIdx)
       .build();
  }
}
