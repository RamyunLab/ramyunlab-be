package ramyunlab_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RamyunInfo {
  private RamyunDTO ramyun;
  private Page<ReviewDTO> review;
}
