package ramyunlab_be.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ramyunlab_be.entity.RecommendEntity;
import ramyunlab_be.entity.ReviewEntity;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.repository.RecommendRepository;
import ramyunlab_be.repository.ReviewRepository;
import ramyunlab_be.repository.UserRepository;

@Service
@Slf4j
public class RecommendService {
    @Autowired
    private RecommendRepository recommendRepository;;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

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
}
