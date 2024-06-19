package ramyunlab_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "회원 정보")
public class UserDTO {
    @Schema(description = "회원 아이디", example = "test123")
    @Pattern(regexp = "(?![0-9])(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z][a-zA-Z0-9].{3,19}", message = "아이디는 숫자로 시작하지 않는 길이 4~20자, 영어 대소문자, 숫자로 이루어진 문자열이어야 합니다.")
    private String userId;

    @Schema(description = "회원 닉네임", example = "test")
    @Pattern(regexp = "^[a-zA-Z가-힣0-9]{2,10}$", message = "닉네임은 특수문자가 들어가지 않은 2~10자 길이의 문자열이어야 합니다.")
    private String nickname;

    @Schema(description = "회원 비밀번호", example = "test123!")
    @Pattern(regexp = "(?![0-9])(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,}", message = "비밀번호는 영어 대소문자, 숫자, 특수문자가 적어도 한 개 이상으로 이루어진 8자 이상의 문자열이어야 합니다.")
    private String password;

    @Schema(description = "회원 인덱스", example = "1")
    private Long userIdx;

    @Schema(description = "회원 토큰", example = "qjhafsdh.safldkjiql.qfeasgfe", nullable = true)
    private String token;

    @Schema(description = "관리자 == 1, 회원 == 0", example = "0", nullable = true)
    private Boolean isAdmin;

    @Schema(description = "탈퇴 회원이면 timestamp 아니면 null", example = "null", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Asia/Seoul")
    private String userDeletedAt;

    private List<GetReviewDTO> reviews;

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
