package ramyunlab_be.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "review")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLDelete(sql = "UPDATE review SET rv_deleted_at = now() WHERE rv_idx = ?")
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
    private String reviewPhotoUrl;

    @Column(name = "rv_created_at", nullable = false)
    @CreationTimestamp
    private Timestamp rvCreatedAt;

    @Column(name = "rv_updated_at")
    @UpdateTimestamp
    private Timestamp rvUpdatedAt;

    @Column(name = "rv_deleted_at")
    private Timestamp rvDeletedAt;

    @Column(name = "rv_recommend_count", columnDefinition = "int default 0")
    @ColumnDefault(value = "0")
    private Integer rvRecommendCount;

    @Column(name = "rv_is_reported", columnDefinition = "tinyint(1) default 0")
    private Boolean rvIsReported;

    @Column(name = "rv_report_count", columnDefinition = "int default 0")
    @ColumnDefault(value = "0")
    private Integer rvReportCount;

    @ManyToOne
    @JoinColumn(name = "u_idx", nullable = false)
    @JsonBackReference
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "r_idx", nullable = false)
    @JsonBackReference
    private RamyunEntity ramyun;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ReportEntity> reports;
}
