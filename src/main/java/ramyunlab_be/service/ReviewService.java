package ramyunlab_be.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ramyunlab_be.dto.ReviewDTO;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.entity.ReviewEntity;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.repository.RamyunRepository;
import ramyunlab_be.repository.ReviewRepository;
import ramyunlab_be.repository.UserRepository;

import java.io.File;
import java.util.UUID;

@Service
@Slf4j
public class ReviewService {

    final private ReviewRepository reviewRepository;
    final private RamyunRepository ramyunRepository;
    final private UserRepository userRepository;

    @Autowired
    public ReviewService(final ReviewRepository reviewRepository,
                         final RamyunRepository ramyunRepository,
                         final UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.ramyunRepository = ramyunRepository;
        this.userRepository = userRepository;
    }

    @Autowired
    private AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloudfront-domain-name}")
    private String cloudfront;

    public ReviewEntity create(final Long ramyunIdx,
                               final String userIdx,
                               final ReviewDTO reviewDTO, MultipartFile file) throws Exception{
        // 유효한 라면 인덱스가 없는 경우(url로 들어갔는데 해당되는 라면이 없는 경우 예외 처리)
        RamyunEntity ramyun = ramyunRepository.findById(ramyunIdx).orElseThrow(()-> new RuntimeException("SERVER ERROR!"));
        // 유효한 유저 인덱스가 없는 경우(토큰 만료)
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(()-> new RuntimeException("로그인을 진행해주세요."));

        Integer rate = Integer.valueOf(reviewDTO.getRate());
        if (file!= null){

        String projectPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "files";

        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
//        log.warn("filename : {}", filename);
//
//        File savedFile = new File(projectPath, filename);
//
//        file.transferTo(savedFile);

            String fileUrl= "https://" + cloudfront + "/" + fileName;
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);


        ReviewEntity reviewWithPhoto = ReviewEntity.builder()
            .reviewContent(reviewDTO.getReviewContent())
            .reviewPhotoUrl(fileUrl)
            .rate(rate)
            .rvCreatedAt(reviewDTO.getRvCreatedAt())
            .ramyun(ramyun)
            .user(user)
            .build();
        return reviewRepository.save(reviewWithPhoto);
        }else{
            ReviewEntity review = ReviewEntity.builder()
            .reviewContent(reviewDTO.getReviewContent())
            .rate(rate)
            .rvCreatedAt(reviewDTO.getRvCreatedAt())
            .ramyun(ramyun)
            .user(user)
            .build();
        return reviewRepository.save(review);
        }
    }

    public ReviewEntity update(final Long ramyunIdx,
                               final Long rvIdx,
                               final String userIdx,
                               final ReviewDTO reviewDTO, MultipartFile file) throws Exception{
        log.warn("service 1 {}, {}, {}, {}, {}", ramyunIdx, rvIdx, userIdx, reviewDTO, file);

        RamyunEntity ramyun = ramyunRepository.findById(ramyunIdx).orElseThrow(()-> new RuntimeException("SERVER ERROR!"));
        // 유효한 리뷰 인덱스가 없는 경우(URL 로 들어갔는데 해당되는 리뷰가 없는 경우 예외 처리)
        ReviewEntity review = reviewRepository.findById(rvIdx).orElseThrow(()-> new RuntimeException("SERVER ERROR!"));

        // 유효한 유저 인덱스가 없는 경우(토큰 만료)
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(()-> new RuntimeException("로그인을 진행해주세요."));

        Integer rate = Integer.valueOf(reviewDTO.getRate());

        if (file!= null){

            String projectPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "files";

            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
//            log.warn("filename : {}", filename);
//
//            File savedFile = new File(projectPath, filename);
//
//            file.transferTo(savedFile);

            String fileUrl= "https://" + cloudfront + "/" + fileName;
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);

            ReviewEntity reviewWithPhoto = ReviewEntity.builder()
                .reviewContent(reviewDTO.getReviewContent())
                .reviewPhotoUrl(fileUrl)
                .rate(rate)
                .rvUpdatedAt(reviewDTO.getRvUpdatedAt())
                .rvIdx(review.getRvIdx())
                .ramyun(ramyun)
                .user(user)
                .rvCreatedAt(review.getRvCreatedAt())
                .build();
            return reviewRepository.save(reviewWithPhoto);
        }else{
            ReviewEntity updatedreview = ReviewEntity.builder()
                .reviewContent(reviewDTO.getReviewContent())
                .rate(rate)
                .rvUpdatedAt(reviewDTO.getRvUpdatedAt())
                .rvCreatedAt(review.getRvCreatedAt())
                .rvIdx(review.getRvIdx())
                .ramyun(ramyun)
                .user(user)
                .build();
            return reviewRepository.save(updatedreview);
        }
    }

    public ReviewEntity delete(final Long rvIdx, final String userIdx){
        ReviewEntity review = reviewRepository.findById(rvIdx)
            .orElseThrow(() -> new RuntimeException("SERVER ERROR!"));
        // 유효한 유저 인덱스가 없는 경우(토큰 만료)
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx))
            .orElseThrow(()-> new RuntimeException("로그인을 진행해주세요."));

        if(user != null && review != null){
            reviewRepository.delete(review);
            return review;
        } else throw new RuntimeException("리뷰 삭제 실패!");
    }
}
