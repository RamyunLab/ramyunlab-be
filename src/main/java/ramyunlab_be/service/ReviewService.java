package ramyunlab_be.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ramyunlab_be.dto.ReportDTO;
import ramyunlab_be.dto.ReviewDTO;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.entity.ReportEntity;
import ramyunlab_be.entity.ReviewEntity;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.repository.RamyunRepository;
import ramyunlab_be.repository.ReportRepository;
import ramyunlab_be.repository.ReviewRepository;
import ramyunlab_be.repository.UserRepository;
import ramyunlab_be.vo.Pagenation;

import java.util.UUID;

@Service
@Slf4j
@Transactional
public class ReviewService {

    final private ReviewRepository reviewRepository;
    final private RamyunRepository ramyunRepository;
    final private UserRepository userRepository;
    final private ReportRepository reportRepository;

    @Autowired
    public ReviewService(final ReviewRepository reviewRepository,
                         final RamyunRepository ramyunRepository,
                         final UserRepository userRepository,
                         final ReportRepository reportRepository) {
        this.reviewRepository = reviewRepository;
        this.ramyunRepository = ramyunRepository;
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
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
            .rvRecommendCount(0)
            .rvReportCount(0)
            .rvIsReported(false)
            .ramyun(ramyun)
            .user(user)
            .build();
        return reviewRepository.save(reviewWithPhoto);
        }else{
            ReviewEntity review = ReviewEntity.builder()
            .reviewContent(reviewDTO.getReviewContent())
            .rate(rate)
            .rvCreatedAt(reviewDTO.getRvCreatedAt())
                .rvReportCount(0)
                .rvRecommendCount(0)
                .rvIsReported(false)
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

    public Page<ReviewDTO> getReviewByRamyun (Long ramyunIdx, Long userIdx, int pageNo){
        try {
            Pageable pageable = PageRequest.of(pageNo - 1, Pagenation.REVIEW_PAGE_SIZE);
            Page<ReviewDTO> result = reviewRepository.findReviewByRamyunIdx(ramyunIdx, userIdx, pageable);
            log.info("service::: {}", result.toString());
            return result;
        }catch (Exception e){
            log.error("ERROR:: {}\n{}\n{}", e.getMessage(), e.getStackTrace(), e.getCause());
            throw new RuntimeException("쿼리 오류");
        }
//        return result.map(this::convert);
    }

    public List<ReviewDTO> getBestReviewByRamyun (Long ramyunIdx, Long userIdx){
        Pageable pageable = PageRequest.of(0, 3);
        Optional<List<ReviewDTO>> result = reviewRepository.findBestReviewByRamyunIdx(ramyunIdx, userIdx, pageable);
        log.info("resutl???? {}",result);
        if(result.isPresent()){
            return result.get();
//            return result.get().stream().map(this::convert).collect(Collectors.toList());
        }
        return null;
    }

//    public Integer goMyReview(Long ramyunIdx, Long userIdx){
//        Integer reviewNo = reviewRepository.getMyReviewNumber(ramyunIdx, userIdx);
//        return (int) Math.ceil((double) reviewNo / Pagenation.REVIEW_PAGE_SIZE);
//    }

    /* 리뷰 추천/추천 취소 시 추천 수 변경 */
    public Integer changeRecommendCount(Long rvIdx, String status){
        Integer count = null;
        log.info("status {}", status);
        if(status.equals("add")){
            count = reviewRepository.plusRecommendCount(rvIdx);
        }else if(status.equals("delete")){
            count = reviewRepository.minusRecommendCount(rvIdx);
        }
        log.info("리뷰 추천///추천삭제    {}", count);
        Integer result = reviewRepository.getReviewRecommendCount(rvIdx);
//        ReviewEntity result = reviewRepository.findByrvIdx(rvIdx);
        return result;
//        return convert(result);
    }

    private ReviewDTO convert(ReviewEntity reviewEntity) {
        return ReviewDTO.builder()
                .rvIdx(reviewEntity.getRvIdx())
                .userIdx(reviewEntity.getUser().getUserIdx())
                .ramyunIdx(reviewEntity.getRamyun().getRamyunIdx())
                .reviewPhotoUrl(reviewEntity.getReviewPhotoUrl())
                .reviewContent(reviewEntity.getReviewContent())
                .rate(reviewEntity.getRate())
                .nickname(reviewEntity.getUser().getNickname())
//                .rate(String.valueOf(reviewEntity.getRate()))
                .rvCreatedAt(reviewEntity.getRvCreatedAt())
                .rvUpdatedAt(reviewEntity.getRvUpdatedAt())
                .rvDeletedAt(reviewEntity.getRvDeletedAt())
                .rvRecommendCount(reviewEntity.getRvRecommendCount())
                .build();
    }

    public ReportEntity complaint(final Long rvIdx,
                                   final String userIdx,
                                   final ReportDTO reportDTO){
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx))
            .orElseThrow(()-> new RuntimeException("로그인을 진행해주세요."));

        ReviewEntity review = reviewRepository.findById(rvIdx)
            .orElseThrow(()-> new RuntimeException("리뷰가 존재하지 않습니다."));

        boolean blockSelfReport = review.getUser().getUserIdx().equals(user.getUserIdx());
        ReportEntity duplicateReport = reportRepository.findReportedReviewByRvIdx(rvIdx, user.getUserIdx());

        if(blockSelfReport){
            throw new RuntimeException("자신의 리뷰는 신고할 수 없습니다.");
        } else if (duplicateReport != null) {
            throw new RuntimeException("이미 신고한 리뷰입니다.");
        } else{

            Long totalReport = reviewRepository.findRvReportCountByRvIdx(rvIdx)
                .orElseThrow(()->new IllegalStateException("SERVER ERROR"));

            log.warn("신고 횟수 {}", totalReport);
            if(totalReport > 4){
                reviewRepository.incrementRvReportCount(rvIdx);
                reviewRepository.changeIsReported(rvIdx);
            }else{
                reviewRepository.incrementRvReportCount(rvIdx);
            }

        ReportEntity report = ReportEntity.builder()
            .reportReason(reportDTO.getReportReason())
            .reportCreatedAt(reportDTO.getReportCreatedAt())
            .user(user)
            .review(review)
            .build();

        return reportRepository.save(report);
        }

    }

}
