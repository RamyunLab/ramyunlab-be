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

    @Transactional
    public ReviewEntity create(final Long ramyunIdx,
                               final String userIdx,
                               final ReviewDTO reviewDTO,
                               final ReviewUploadPhotoDTO reviewUploadPhotoDTO){
        // 유효한 라면 인덱스가 없는 경우(url로 들어갔는데 해당되는 라면이 없는 경우 예외 처리)
        RamyunEntity ramyun = ramyunRepository.findById(ramyunIdx).orElseThrow(()-> new RuntimeException("SERVER ERROR!"));
        // 유효한 유저 인덱스가 없는 경우(토큰 만료)
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(()-> new RuntimeException("로그인을 진행해주세요."));

//        if(reviewUploadPhotoDTO.getFiles() != null && !reviewUploadPhotoDTO.getFiles().isEmpty()){
//            for(MultipartFile file : reviewUploadPhotoDTO.getFiles()){
//                UUID uuid = UUID.randomUUID();
//                String imageFileName = uuid + "_" + file.getOriginalFilename();
//
//                File destinationFile = new File(Folderename + imageFileName);
//            }
//        }
//
//        try{
//            MultipartFile file;
//            file.transferTo(destinationFile);
//        }

        ReviewEntity review = ReviewEntity.builder()
            .reviewContent(reviewDTO.getReviewContent())
            .reviewPhotoUrls(reviewDTO.getReviewPhotoUrls())
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
            .reviewPhotoUrls(reviewDTO.getReviewPhotoUrls())
            .rate(reviewDTO.getRate())
            .rvUpdatedAt(reviewDTO.getRvUpdatedAt())
            .user(user)
            .build();
        return reviewRepository.save(review);
    }

//    public ReviewEntity uploadFile(MultipartFile file) throws IOError {
//        Path uploadPath = Paths.get(uploadFile);
//        if(Files.exists(uploadPath)){
//            Files.createDirectories(uploadPath);
//        }
//
//        String FileName = file.getOriginalFilename();
//        Path filePath = uploadPath.resolve(fileName);
//        Files.copy(file.getInputStream(), filePath);
//
//        return FileName;
//    }
}
