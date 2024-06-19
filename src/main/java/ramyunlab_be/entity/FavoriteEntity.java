package ramyunlab_be.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.annotations.Formula;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorites")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoriteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fav_idx", updatable = false)
    private Long favIdx;

    @Column(name = "fav_created_at", nullable = false)
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Asia/Seoul")
    private Timestamp favCreatedAt;

    @Formula("(SELECT COALESCE(AVG(rv.rv_rate), 0) FROM review rv WHERE rv.r_idx = r_idx)")
    private Double avgRate;

    @Formula("(SELECT COALESCE(COUNT(rv.rv_rate), 0) FROM review rv WHERE rv.r_idx = r_idx)")
    private Integer reviewCount;

    @ManyToOne
    @JoinColumn(name = "u_idx", nullable = false)
    @JsonBackReference
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "r_idx", nullable = false)
    @JsonBackReference
    private RamyunEntity ramyun;
}
