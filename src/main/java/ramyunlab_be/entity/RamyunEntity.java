package ramyunlab_be.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ramyun")
public class RamyunEntity {
    private long idx;
    private String ramyunName;
    private String ramyunImg;
    private Integer ramyunKcal;
    private Boolean noodle;
    private Boolean isCup;
    private Boolean cooking;
    private Integer gram;
    private Integer ramyunNa;
    private Integer scoville;
}
