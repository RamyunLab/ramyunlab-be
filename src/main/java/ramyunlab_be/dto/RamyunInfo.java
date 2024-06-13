package ramyunlab_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "라면 페이지 정보", description = "라면 페이지 정보 (라면, 리뷰)")
public class RamyunInfo {
  @Schema(name = "라면 정보", description = "라면 정보 DTO")
  private RamyunDTO ramyun;
  @Schema(name = "리뷰 정보", description = "특정 라면에 대한 리뷰 정보 DTO")
  private Page<ReviewDTO> review;
}
