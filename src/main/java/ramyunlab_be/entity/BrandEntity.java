package ramyunlab_be.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "brand")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "b_idx")
    private Long brandIdx;

    @Column(name = "b_name", nullable = false)
    private String brandName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Asia/Seoul")
    @Column(name = "b_deleted_at")
    private Timestamp brandDeletedAt;

    @OneToMany(mappedBy = "brand",  cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RamyunEntity> ramyuns;

}

