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
        RamyunEntity ramyun = ramyunRepository.findById(ramyunIdx).orElseThrow(()-> new RuntimeException("errr1"));
        log.warn("userIdx {}", userIdx);
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(()-> new RuntimeException("errr2"));


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
}
