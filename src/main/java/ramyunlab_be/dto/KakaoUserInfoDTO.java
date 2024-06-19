package ramyunlab_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "소셜 로그인 회원 정보")
public class KakaoUserInfoDTO {
    private String kakaoId;
    private String kakaoName;
    private String accessToken;

    public static String extractUserId(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return email.split("@")[0];
    }
}
