package ramyunlab_be.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ramyun")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamyunEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="r_idx", updatable = false)
    private Long ramyunIdx;

    @Column(name = "r_name", nullable = false, length = 20)
    private String ramyunName;

    @Column(name = "r_img")
    private String ramyunImg;

    @Column(name = "r_kcal", nullable = false)
    private Integer ramyunKcal;

    @Column(name = "r_noodle", nullable = false)
    private Boolean noodle;

    @Column(name = "r_is_cup", nullable = false)
    private Boolean isCup;

    @Column(name = "r_cooking", nullable = false)
    private Boolean cooking;

    @Column(name = "r_gram", nullable = false)
    private Integer gram;

    @Column(name = "r_Na", nullable = false)
    private Integer ramyunNa;

    @Column(name = "r_scoville")
    private Integer scoville;

    @ManyToOne
    @JoinColumn(name = "b_idx", nullable = false)
    @JsonBackReference
    private BrandEntity brand;

    @OneToMany(mappedBy = "ramyun",  cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ReviewEntity> reviews;

    @OneToMany(mappedBy = "ramyun", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<FavoriteEntity> favorites;

    @Override
    public String toString () {
        return "RamyunEntity{" +
               "brand=" + brand +
               ", scoville=" + scoville +
               ", ramyunNa=" + ramyunNa +
               ", gram=" + gram +
               ", cooking=" + cooking +
               ", isCup=" + isCup +
               ", noodle=" + noodle +
               ", ramyunKcal=" + ramyunKcal +
               ", ramyunImg='" + ramyunImg + '\'' +
               ", ramyunName='" + ramyunName + '\'' +
               ", ramyunIdx=" + ramyunIdx +
               '}';
    }
}
