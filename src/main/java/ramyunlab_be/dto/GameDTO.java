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
@Schema(name="GameDTO", description = "게임에 필요한 라면 정보")
public class GameDTO {
  @Schema(name = "id")
  private Long r_idx;

  @Schema(name = "name")
  private String r_name;

  @Schema(name = "image")
  private String r_img;

  @Schema(name = "scoville")
  private int r_scoville;
}
