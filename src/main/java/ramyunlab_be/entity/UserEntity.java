package ramyunlab_be.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLDelete(sql = "UPDATE user SET u_deleted_at = now() WHERE u_idx = ?")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_idx", updatable = false)
    private Long userIdx;

    @Column(name = "u_Id", nullable = false, length = 20)
    private String userId;

    @Column(name = "u_Nickname", nullable = false, length = 30)
    private String nickname;

    @Column(name = "u_Password", nullable = false)
    private String password;

    @Column(name = "u_is_admin", nullable = false)
    @Builder.Default
    private Boolean isAdmin=false;

    @Column(name = "u_deleted_at")
    private Timestamp userDeletedAt;

    @OneToMany(mappedBy = "user",  cascade = CascadeType.ALL)
    @JsonBackReference
    private List<ReviewEntity> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<RecommendEntity> recommends;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<FavoriteEntity> favorites;
}