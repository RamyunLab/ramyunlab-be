package ramyunlab_be.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUploadPhotoDTO {
    private String files;

    @Override
    public String toString() {
        return "ReviewUploadPhotoDTO{" +
            "files=" + files +
            '}';
    }
}
