package ramyunlab_be.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDTO {
    private String userId;
    private String nickname;
    private String password;
    private Long idx;
    private String token;

    @Override
    public String toString() {
        return "UserDTO{" +
            "userId='" + userId + '\'' +
            ", nickname='" + nickname + '\'' +
            ", password='" + password + '\'' +
            ", idx=" + idx +
            ", token='" + token + '\'' +
            '}';
    }
}
