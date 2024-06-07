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
//    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^(?=[a-zA-Z])[a-zA-Z0-9_-]{4,20}$", message = "아이디는 숫자로 시작하지 않는 길이 4~20자, 영어 대소문자, 숫자로 이루어진 문자열이어야 합니다.")
    private String userId;

    @Pattern(regexp = "^[a-zA-Z가-힣0-9]{2,10}$", message = "닉네임은 특수문자가 들어가지 않은 2~10자 길이의 문자열이어야 합니다.")
    private String nickname;
//    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp ="^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()-_=+\\\\|/.,<>?:;'\"{}\\\\[\\\\]\\\\\\\\]).{8,}$",
        message = "비밀번호는 숫자로 시작하지 않는 길이 8자 이상의 영어 대소문자, 숫자, 특수문자로 이루어진 문자열이어야 합니다.")
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
