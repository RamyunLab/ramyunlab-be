package ramyunlab_be.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ramyunlab_be.dto.ReviewDTO;
import ramyunlab_be.entity.RecommendEntity;
import ramyunlab_be.entity.ReviewEntity;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.repository.RecommendRepository;
import ramyunlab_be.repository.ReviewRepository;
import ramyunlab_be.repository.UserRepository;

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

    public RecommendEntity create(final Long rvIdx, final String userIdx){

        ReviewEntity review = reviewRepository.findById(rvIdx)
            .orElseThrow(()-> new RuntimeException("SERVER ERROR!"));

        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx))
            .orElseThrow(()-> new RuntimeException("로그인을 진행해주세요."));

        RecommendEntity recommend = RecommendEntity.builder()
            .review(review)
            .user(user)
            .build();

        return recommendRepository.save(recommend);
    }

    public RecommendEntity delete(final Long recommendIdx, final String userIdx) {
        RecommendEntity recommend = recommendRepository.findById(recommendIdx)
            .orElseThrow(() -> new RuntimeException("SERVER ERROR!"));
        // 유효한 유저 인덱스가 없는 경우(토큰 만료)
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx))
            .orElseThrow(() -> new RuntimeException("로그인을 진행해주세요."));

        if (user != null && recommend != null) {
            recommendRepository.delete(recommend);
            return recommend;
        } else throw new RuntimeException("추천 삭제 실패!");
    }
}
