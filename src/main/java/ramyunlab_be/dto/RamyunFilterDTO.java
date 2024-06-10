package ramyunlab_be.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.Getter;

@Getter
@Data

@Schema(name = "SearchDTO", description = "검색에 필요한 쿼리스트링 관련 DTO")
public class RamyunFilterDTO {
  @Schema(name="brandName", example = "삼양")
  private String brandName;

  @Schema(name = "noodle")
  private List<Boolean> noodle;

  @Schema(name="isCup")
  private List<Boolean> isCup;

  @Schema(name="cooking")
  private List<Boolean> cooking;

  @Schema(name = "ramyunKcal")
  private Integer ramyunKcal;

  @Schema(name="gram")
  private Integer gram;

  @Schema(name="ramyunNa")
  private Integer ramyunNa;

  @QueryProjection

  public RamyunFilterDTO (String brandName, List<Boolean> noodle, List<Boolean> isCup, List<Boolean> cooking,
                          Integer ramyunKcal, Integer gram, Integer ramyunNa) {
    this.brandName = brandName;
    this.noodle = noodle;
    this.isCup = isCup;
    this.cooking = cooking;
    this.ramyunKcal = ramyunKcal;
    this.gram = gram;
    this.ramyunNa = ramyunNa;
  }
}
