package ramyunlab_be.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ramyunlab_be.dto.ReviewDTO;
import ramyunlab_be.dto.ReviewUploadPhotoDTO;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.entity.ReviewEntity;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.repository.RamyunRepository;
import ramyunlab_be.repository.ReviewRepository;
import ramyunlab_be.repository.UserRepository;

import java.io.File;
import java.io.IOError;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RamyunRepository ramyunRepository;

    @Autowired
    private UserRepository userRepository;


    public ReviewEntity create(final Long ramyunIdx,
                               final String userIdx,
                               final ReviewDTO reviewDTO, MultipartFile file) throws Exception{
        // 유효한 라면 인덱스가 없는 경우(url로 들어갔는데 해당되는 라면이 없는 경우 예외 처리)
        RamyunEntity ramyun = ramyunRepository.findById(ramyunIdx).orElseThrow(()-> new RuntimeException("SERVER ERROR!"));
        // 유효한 유저 인덱스가 없는 경우(토큰 만료)
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(()-> new RuntimeException("로그인을 진행해주세요."));

        if (file!= null){

        String projectPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "files";

        UUID uuid = UUID.randomUUID();
        String filename = uuid + "_" + file.getOriginalFilename();
        log.warn("filename : {}", filename);

        File savedFile = new File(projectPath, filename);

        file.transferTo(savedFile);

        ReviewEntity reviewWithPhoto = ReviewEntity.builder()
            .reviewContent(reviewDTO.getReviewContent())
            .reviewPhotoUrl(filename)
            .rate(reviewDTO.getRate())
            .rvCreatedAt(reviewDTO.getRvCreatedAt())
            .ramyun(ramyun)
            .user(user)
            .build();
        return reviewRepository.save(reviewWithPhoto);
        }else{
            ReviewEntity review = ReviewEntity.builder()
            .reviewContent(reviewDTO.getReviewContent())
            .rate(reviewDTO.getRate())
            .rvCreatedAt(reviewDTO.getRvCreatedAt())
            .ramyun(ramyun)
            .user(user)
            .build();
        return reviewRepository.save(review);
        }
    }

    public ReviewEntity update(final Long rvIdx, final String userIdx, final ReviewDTO reviewDTO){
        // 유효한 리뷰 인덱스가 없는 경우(URL 로 들어갔는데 해당되는 리뷰가 없는 경우 예외 처리)
        ReviewEntity review = reviewRepository.findById(rvIdx).orElseThrow(()-> new RuntimeException("SERVER ERROR!"));

        // 유효한 유저 인덱스가 없는 경우(토큰 만료)
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(()-> new RuntimeException("로그인을 진행해주세요."));
        review = review.toBuilder()
            .reviewContent(reviewDTO.getReviewContent())
//            .reviewPhotoUrl(reviewDTO.getReviewPhotoUrl())
            .rate(reviewDTO.getRate())
            .rvUpdatedAt(reviewDTO.getRvUpdatedAt())
            .user(user)
            .build();
        return reviewRepository.save(review);
    }

    public ReviewEntity uploadPhoto(final Long ramyunIdx,
                               final String userIdx,
                               MultipartFile file ) throws Exception{

//        if (file == null || file.isEmpty()) {
//            throw new IllegalArgumentException("파일이 업로드되지 않았습니다.");
//        }
        // 유효한 라면 인덱스가 없는 경우(url로 들어갔는데 해당되는 라면이 없는 경우 예외 처리)
        RamyunEntity ramyun = ramyunRepository.findById(ramyunIdx).orElseThrow(()-> new RuntimeException("SERVER ERROR!"));
        // 유효한 유저 인덱스가 없는 경우(토큰 만료)
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(()-> new RuntimeException("로그인을 진행해주세요."));
        String projectPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "files";

        UUID uuid = UUID.randomUUID();
        String filename = uuid + "_" + file.getOriginalFilename();
        log.warn("filename : {}", filename);

        File savedFile = new File(projectPath, filename);

        file.transferTo(savedFile);

        ReviewEntity review = ReviewEntity.builder()
            .reviewPhotoUrl(filename)
            .ramyun(ramyun)
            .user(user)
            .build();
        return reviewRepository.save(review);
    }
}
