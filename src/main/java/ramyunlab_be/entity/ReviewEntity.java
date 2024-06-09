package ramyunlab_be.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "review")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rv_idx", updatable = false)
    private Long rvIdx;

    @Column(name = "rv_content")
    private String reviewContent;

    @Column(name = "rv_rate", nullable = false)
    private Integer rate;

    @Column(name="rv_photo", length = 200)
    private List<String> reviewPhotoUrls;

    @Column(name = "rv_created_at", nullable = false)
    @CreationTimestamp
    private Timestamp rvCreatedAt;

    @Column(name = "rv_updated_at")
    @UpdateTimestamp
    private Timestamp rvUpdatedAt;

    @Column(name = "rv_deleted_at")
    private Timestamp rvDeletedAt;

    @ManyToOne
    @JoinColumn(name = "u_idx", nullable = false)
    @JsonBackReference
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "r_idx", nullable = false)
    @JsonBackReference
    private RamyunEntity ramyun;
}
