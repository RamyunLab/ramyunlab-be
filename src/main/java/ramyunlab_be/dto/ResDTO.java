package ramyunlab_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
//@AllArgsConstructor
@Schema(name = "ResDTO", description = "응답 값의 기본 형식을 담은 DTO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResDTO<T> {

  @Schema(name="상태 코드", nullable = false)
  private Integer statusCode;

  @Schema(name="메시지", description = "요청 실행 결과 메시지", nullable = false)
  private String message;

  @Schema(name="데이터", description = "응답 시 전달해야 하는 데이터", nullable = true)
  private T data;

  public ResDTO(Integer statusCode, String message){
    this.statusCode = statusCode;
    this.message = message;
  }

  // 생성자
  @Builder
  public ResDTO(Integer statusCode, String message, T data) {
    this.statusCode = statusCode;
    this.message = message;
    this.data = data;
  }
}
