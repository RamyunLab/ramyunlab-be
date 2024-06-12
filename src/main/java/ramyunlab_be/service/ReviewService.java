package ramyunlab_be.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import io.swagger.v3.oas.annotations.Operation;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.dto.ReviewDTO;
import ramyunlab_be.dto.ReviewDTO;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.entity.ReviewEntity;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.repository.RamyunRepository;
import ramyunlab_be.repository.ReviewRepository;
import ramyunlab_be.repository.UserRepository;
import ramyunlab_be.vo.Pagenation;

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
        RamyunEntity ramyun = ramyunRepository.findById(ramyunIdx).orElseThrow(()-> new RuntimeException("해당 상품이 존재하지 않습니다."));
        // 유효한 유저 인덱스가 없는 경우(토큰 만료)
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(()-> new RuntimeException("로그인을 진행해주세요."));

        Integer rate = Integer.valueOf(reviewDTO.getRate());
        if (file!= null){
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();

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

        RamyunEntity ramyun = ramyunRepository.findById(ramyunIdx).orElseThrow(()-> new RuntimeException("SERVER ERROR!"));
        // 유효한 리뷰 인덱스가 없는 경우(URL 로 들어갔는데 해당되는 리뷰가 없는 경우 예외 처리)
        ReviewEntity review = reviewRepository.findById(rvIdx).orElseThrow(()-> new RuntimeException("SERVER ERROR!"));

        // 유효한 유저 인덱스가 없는 경우(토큰 만료)
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(()-> new RuntimeException("로그인을 진행해주세요."));

        Integer rate = Integer.valueOf(reviewDTO.getRate());

        if (file!= null){


            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();

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

    public Page<ReviewDTO> getMyReviewList(Integer pageNo, String userIdx) {
        PageRequest pageRequest = PageRequest.of(pageNo - 1, Pagenation.PAGE_SIZE, Sort.by(Sort.Direction.DESC, "rvCreatedAt"));

        Page<ReviewEntity> result = reviewRepository.findByUser_UserIdx(pageRequest, Long.valueOf(userIdx));

        return result.map(this::convert);
    }


    @Operation(summary = "라면별 리뷰 조회", description = "라면 상세페이지 리뷰 조회")
    public Page<ReviewDTO> getReviewByRamyun (Long id, int pageNo){
        Pageable pageable = PageRequest.of(pageNo - 1, Pagenation.REVIEW_PAGE_SIZE);
        Page<ReviewEntity> result = reviewRepository.findReviewByRamyunIdx(id, pageable);
      return result.map(review -> ReviewDTO.builder()
                                             .rvIdx(review.getRvIdx())
                                             .userIdx(review.getUser().getUserIdx())
                                             .ramyunIdx(review.getRamyun().getRamyunIdx())
                                             .reviewContent(review.getReviewContent())
                                             .rate(Integer.toString(review.getRate()))
                                             .reviewPhotoUrl(review.getReviewPhotoUrl())
                                             .rvCreatedAt(review.getRvCreatedAt())
                                             .rvRecommendCount(review.getRvRecommendCount())
                                             .build());
    }

    private ReviewDTO convert(ReviewEntity reviewEntity) {
        return ReviewDTO.builder()
                .rvIdx(reviewEntity.getRvIdx())
                .userIdx(reviewEntity.getUser().getUserIdx())
                .ramyunIdx(reviewEntity.getRamyun().getRamyunIdx())
                .reviewPhotoUrl(reviewEntity.getReviewPhotoUrl())
                .reviewContent(reviewEntity.getReviewContent())
                .rate(String.valueOf(reviewEntity.getRate()))
                .rvCreatedAt(reviewEntity.getRvCreatedAt())
                .rvUpdatedAt(reviewEntity.getRvUpdatedAt())
                .rvDeletedAt(reviewEntity.getRvDeletedAt())
                .build();
    }
}
