package ramyunlab_be.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ramyunlab_be.dto.RecReviewDTO;
import ramyunlab_be.dto.RecommendDTO;
import ramyunlab_be.entity.RecommendEntity;
import ramyunlab_be.entity.ReviewEntity;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.repository.RecommendRepository;
import ramyunlab_be.repository.ReviewRepository;
import ramyunlab_be.repository.UserRepository;
import ramyunlab_be.vo.Pagination;

@Service
@Slf4j
public class RecommendService {

    final private RecommendRepository recommendRepository;;
    final private ReviewRepository reviewRepository;
    final private UserRepository userRepository;


    @Autowired
    public RecommendService(final RecommendRepository recommendRepository,
                            final ReviewRepository reviewRepository,
                            final UserRepository userRepository) {
        this.recommendRepository = recommendRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    public RecommendDTO create(final Long rvIdx, final String userIdx){

        ReviewEntity review = reviewRepository.findById(rvIdx)
            .orElseThrow(()-> new RuntimeException("SERVER ERROR!"));

        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx))
            .orElseThrow(()-> new RuntimeException("로그인을 진행해주세요."));

        RecommendEntity recommend = RecommendEntity.builder().review(review).user(user).build();
        RecommendEntity addRecommend = recommendRepository.save(recommend);

        return RecommendDTO.builder()
                           .recommendIdx(addRecommend.getRecommendIdx())
                           .userIdx(addRecommend.getUser().getUserIdx())
                           .recCreatedAt(addRecommend.getRecCreatedAt())
                           .reviewIdx(addRecommend.getReview().getRvIdx())
                           .build();
    }

    public RecommendEntity delete(final Long rvIdx, final Long userIdx) {
        RecommendEntity recommend = recommendRepository.findRecommend(rvIdx, userIdx)
            .orElseThrow(() -> new RuntimeException("SERVER ERROR!"));
        // 유효한 유저 인덱스가 없는 경우(토큰 만료)
        UserEntity user = userRepository.findByUserIdx(userIdx)
            .orElseThrow(() -> new RuntimeException("로그인을 진행해주세요."));

        if (user != null && recommend != null) {
            recommendRepository.delete(recommend);
            return recommend;
        } else throw new RuntimeException("추천 삭제 실패!");
    }

    // 내가 공감한 리뷰 목록 호출
    public Page<RecReviewDTO> getMyRecReview(Integer pageNo, String userIdx) {
        PageRequest pageRequest = PageRequest.of(pageNo - 1, Pagination.PAGE_SIZE, Sort.by(Sort.Direction.DESC, "recCreatedAt"));

        Page<RecommendEntity> result = recommendRepository.findByUser_UserIdx(pageRequest, Long.valueOf(userIdx));

        return result.map(recommendEntity -> {
            ReviewEntity review = recommendEntity.getReview();
            return convert(recommendEntity,review);
        });
    }

    private RecReviewDTO convert(RecommendEntity recommendEntity, ReviewEntity reviewEntity) {
        return RecReviewDTO.builder()
                .recommendIdx(recommendEntity.getRecommendIdx())
                .recCreatedAt(recommendEntity.getRecCreatedAt())
                .userIdx(recommendEntity.getUser().getUserIdx()) // 예시로 UserEntity에서 UserIdx를 가져오는 경우
                .ramyunIdx(reviewEntity.getRamyun().getRamyunIdx()) // 예시로 RamyunEntity에서 RamyunIdx를 가져오는 경우
                .reviewIdx(reviewEntity.getRvIdx())
                .reviewContent(reviewEntity.getReviewContent())
                .rate(reviewEntity.getRate().toString()) // 별점이 String 타입인 경우
                .reviewPhotoUrl(reviewEntity.getReviewPhotoUrl())
                .rvCreatedAt(reviewEntity.getRvCreatedAt())
                .rvUpdatedAt(reviewEntity.getRvUpdatedAt())
                .rvDeletedAt(reviewEntity.getRvDeletedAt())
                .rvRecommendCount(reviewEntity.getRvRecommendCount())
                .build();
    }
}
