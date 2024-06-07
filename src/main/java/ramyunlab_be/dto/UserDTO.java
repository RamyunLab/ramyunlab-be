package ramyunlab_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^(?=[a-zA-Z])[a-zA-Z0-9_-]{4,20}$", message = "Invalid userId")
    private String userId;

    private String nickname;

    @Pattern(regexp ="^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()-_=+\\\\|/.,<>?:;'\"{}\\\\[\\\\]\\\\\\\\]).{8,}$", message = "Invalid password")
    private String password;
    private Long userIdx;
    private String token;

    @Override
    public String toString() {
        return "UserDTO{" +
            "userId='" + userId + '\'' +
            ", nickname='" + nickname + '\'' +
            ", password='" + password + '\'' +
            ", userIdx=" + userIdx +
            ", token='" + token + '\'' +
            '}';
    }
}
