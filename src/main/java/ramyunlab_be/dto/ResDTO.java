package ramyunlab_be.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResDTO<T> {
  private int statusCode;
  private String message;
  private T data;

}
