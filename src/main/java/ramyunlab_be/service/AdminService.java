package ramyunlab_be.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ramyunlab_be.dto.BrandDTO;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.entity.BrandEntity;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.entity.ReviewEntity;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.repository.BrandRepository;
import ramyunlab_be.repository.RamyunRepository;
import ramyunlab_be.repository.UserRepository;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static ramyunlab_be.entity.QBrandEntity.brandEntity;
import static ramyunlab_be.entity.QRamyunEntity.ramyunEntity;

@Service
@Slf4j
public class AdminService {


    private static RamyunRepository ramyunRepository = null;
    final private UserRepository userRepository;
    final private BrandRepository brandRepository;

    @Autowired
    public AdminService(final RamyunRepository ramyunRepository,
                        final UserRepository userRepository,
                        final BrandRepository brandRepository) {
        this.ramyunRepository = ramyunRepository;
        this.userRepository = userRepository;
        this.brandRepository = brandRepository;
    }

    @Autowired
    private AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloudfront-domain-name}")
    private String cloudfront;

    public static Page<RamyunEntity> getGoodsList(Pageable pageable) {
        return ramyunRepository.findAll(pageable);
    }

    public RamyunEntity addGoods(final RamyunDTO ramyunDTO,
                                 final MultipartFile file,
                                 final String userIdx) throws Exception {
        // 유효한 관리자 인덱스가 없는 경우(토큰 만료)
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(() -> new RuntimeException("관리자로 로그인을 진행해주세요."));

        BrandEntity brand = brandRepository.findByBrandName(ramyunDTO.getBrandName()).orElseThrow(() -> new RuntimeException("등록되지 않은 브랜드입니다."));
//        if(brandEntity != null && ramyunDTO.getBrandIdx() == null){
//            brand = brandRepository.save(brand);
//        }

        if (file != null) {

            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            String fileUrl = "https://" + cloudfront + "/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);



            RamyunEntity newGoods = RamyunEntity.builder()
                .ramyunIdx(ramyunDTO.getRamyunIdx())
                .ramyunName(ramyunDTO.getRamyunName())
                .ramyunImg(fileUrl)
                .ramyunNa(ramyunDTO.getRamyunNa())
                .isCup(ramyunDTO.getIsCup())
                .noodle(ramyunDTO.getNoodle())
                .gram(ramyunDTO.getGram())
                .ramyunKcal(ramyunDTO.getRamyunKcal())
                .brand(brand)
                .build();
            return ramyunRepository.save(newGoods);
        } else if(file == null && ramyunDTO.getRamyunImg()!= null){
            throw new RuntimeException("이미지를 추가해주세요.");
        }else
            throw new RuntimeException("상품 등록 실패");
    }

}
