package ramyunlab_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ramyunlab_be.entity.RamyunEntity;

import java.sql.Timestamp;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FavoriteDTO", description = "찜 목록")
public class FavoriteDTO{
    private Long favIdx;
    private Long userIdx;
    private Long ramyunIdx;
    private String ramyunName;
    private String ramyunImg;
    private Timestamp favCreatedAt;
}
