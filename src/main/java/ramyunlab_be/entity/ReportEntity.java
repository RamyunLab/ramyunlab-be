package ramyunlab_be.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "report")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rp_idx", updatable = false)
    private Long reportIdx;

    @Column(name = "rp_reason", nullable = false, length = 500)
    private String reportReason;

    @Column(name = "rp_created_at", nullable = false)
    @CreationTimestamp
    private Timestamp reportCreatedAt;

    @ManyToOne
    @JoinColumn(name = "u_idx", nullable = false)
    @JsonBackReference
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "rv_idx", nullable = false)
    @JsonBackReference
    private ReviewEntity review;
}
