package ramyunlab_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_idx", updatable = false)
    private long idx;

    @Column(name = "u_Id", nullable = false, length = 20)
    private String userId;

    @Column(name = "u_Nickname", nullable = false, length = 30)
    private String nickname;

    @Column(name = "u_Password", nullable = false)
    private String password;

}
