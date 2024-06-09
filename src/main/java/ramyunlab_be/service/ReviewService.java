package ramyunlab_be.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ramyunlab_be.dto.ReviewDTO;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.entity.ReviewEntity;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.repository.RamyunRepository;
import ramyunlab_be.repository.ReviewRepository;
import ramyunlab_be.repository.UserRepository;

@Service
@Slf4j
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RamyunRepository ramyunRepository;

    @Autowired
    private UserRepository userRepository;

    public ReviewEntity create(final Long ramyunIdx, final String userIdx, final ReviewDTO reviewDTO){
        // 유효한 라면 인덱스가 없는 경우(url로 들어갔는데 해당되는 라면이 없는 경우 예외 처리)
        RamyunEntity ramyun = ramyunRepository.findById(ramyunIdx).orElseThrow(()-> new RuntimeException("SERVER ERROR!"));
        // 유효한 유저 인덱스가 없는 경우(토큰 만료)
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(()-> new RuntimeException("로그인을 진행해주세요."));

        ReviewEntity review = ReviewEntity.builder()
            .reviewContent(reviewDTO.getReviewContent())
            .reviewPhoto(reviewDTO.getReviewPhoto())
            .rate(reviewDTO.getRate())
            .rvCreatedAt(reviewDTO.getRvCreatedAt())
            .ramyun(ramyun)
            .user(user)
            .build();
        return reviewRepository.save(review);
    }

    public ReviewEntity update(final Long rvIdx, final String userIdx, final ReviewDTO reviewDTO){
        ReviewEntity review = reviewRepository.findById(rvIdx).orElseThrow(()-> new RuntimeException("errrr4324"));
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(()-> new RuntimeException("errr4"));
        review = review.toBuilder()
            .reviewContent(reviewDTO.getReviewContent())
            .reviewPhoto(reviewDTO.getReviewPhoto())
            .rate(reviewDTO.getRate())
            .rvUpdatedAt(reviewDTO.getRvUpdatedAt())
            .user(user)
            .build();
        return reviewRepository.save(review);
    }
}
