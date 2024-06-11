package ramyunlab_be.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
@Schema(name = "SearchDTO", description = "검색에 필요한 쿼리스트링 관련 DTO")
public class RamyunFilterDTO {

  @Schema(name="name", example = "불닭")
  private String name;

  @Schema(name="brand", example = "1")
  private List<Long> brand;

  @Schema(name = "noodle")
  private List<Boolean> noodle;

  @Schema(name="isCup")
  private List<Boolean> isCup;

  @Schema(name="cooking")
  private List<Boolean> cooking;

  @Schema(name = "ramyunKcal")
  private List<Integer> kcal;

  @Schema(name="gram")
  private List<Integer> gram;

  @Schema(name="ramyunNa")
  private List<Integer> na;

  @QueryProjection

  public RamyunFilterDTO (String name, List<Long> brand, List<Boolean> noodle, List<Boolean> isCup,
                          List<Boolean> cooking, List<Integer> kcal, List<Integer> gram, List<Integer> na) {
    this.name = name;
    this.brand = brand;
    this.noodle = noodle;
    this.isCup = isCup;
    this.cooking = cooking;
    this.kcal = kcal;
    this.gram = gram;
    this.na = na;
  }
}
