package ramyunlab_be.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "brand")
public class BrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandIdx;

    @Column(name = "b_name", nullable = false)
    private Integer brand;

    @OneToMany(mappedBy = "brand",  cascade = CascadeType.ALL)
    @JsonBackReference
    private List<RamyunEntity> ramyuns;

}

