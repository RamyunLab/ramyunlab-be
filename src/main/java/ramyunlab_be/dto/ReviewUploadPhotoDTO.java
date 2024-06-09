package ramyunlab_be.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ReviewUploadPhotoDTO {
    private List<MultipartFile> files;
}
