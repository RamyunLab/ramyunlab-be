package ramyunlab_be.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

  private int statusCode;
  private String message;
  private T data;

  // data 없는 경우
  public ApiResponse(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
    this.data = null;
  }

  // 응답코드, 메시지, 데이터 전부 있을 경우
  public static <T> ApiResponse<T> response(int statusCode, String message, T data){
    return response(statusCode, message, data);
  }

  // 응답코드, 메시지만 존재할 경우
  public static <T> ApiResponse<T> response(int statusCode, String message){
    return response(statusCode, message);
  }
}
