package ramyunlab_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RamyunListItemDTO {
  private Long ramyunIdx;
  private String ramyunName;
  private String ramyunImg;
  private Double avgRate;
  private int reviewCount;
  private boolean isLiked;

}
