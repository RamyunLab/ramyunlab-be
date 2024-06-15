package ramyunlab_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
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
public class RamyunDetailDTO {
  @Schema(name = "라면 정보", description = "라면 정보 DTO")
  private RamyunDTO ramyun;
  @Schema(name = "리뷰 목록", description = "특정 라면에 대한 리뷰 목록")
  private Page<ReviewDTO> review;
  @Schema(name = "찜 여부", description = "사용자의 찜 여부를 확인\n비로그인 유저 - false, 로그인 유저 - 찜 여부 조회 후 true/false 판별")
  private Boolean isLiked;
  @Schema(name = "베스트 리뷰 목록", description = "특정 라면에 대한 베스트 리뷰 목록")
  private List<ReviewDTO> bestReview;
}
