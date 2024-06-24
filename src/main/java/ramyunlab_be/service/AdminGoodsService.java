package ramyunlab_be.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ramyunlab_be.dto.BrandDTO;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.dto.RamyunProjection;
import ramyunlab_be.dto.UserProjection;
import ramyunlab_be.entity.BrandEntity;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.repository.BrandRepository;
import ramyunlab_be.repository.RamyunRepository;
import ramyunlab_be.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AdminGoodsService {


    private static RamyunRepository ramyunRepository = null;
    final private UserRepository userRepository;
    final private BrandRepository brandRepository;

    @Autowired
    public AdminGoodsService(final RamyunRepository ramyunRepository,
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
    private static final String GOODS_DIR = "img/ramyun/";

    public static Page<RamyunProjection> getGoodsList(Pageable pageable) {
        return ramyunRepository.findAllWithBrand(pageable);
    }

    public RamyunProjection getGoods(final Long ramyunIdx,
                                 final String userIdx) {
        userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(() -> new RuntimeException("관리자로 로그인을 진행해주세요."));

        RamyunProjection result = ramyunRepository.findByRamyunIdx(Long.valueOf(ramyunIdx)).orElseThrow(()-> new RuntimeException("SERVER ERROR!"));
        return result;
    }

    public List<RamyunProjection> searchGoods(final String keyword,
                                           final String userIdx) {
        userRepository.findByUserIdx(Long.valueOf(userIdx))
            .orElseThrow(() -> new RuntimeException("로그인을 해주세요."));

        return ramyunRepository.searchBykeyword(keyword);
    }

    public RamyunEntity addGoods(final RamyunDTO ramyunDTO,
                                 final MultipartFile file,
                                 final String userIdx) throws Exception {
        // 유효한 관리자 인덱스가 없는 경우(토큰 만료)
        userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(() -> new RuntimeException("관리자로 로그인을 진행해주세요."));

        BrandEntity brand = brandRepository.findByBrandName(ramyunDTO.getBrandName()).orElseThrow(() -> new RuntimeException("등록되지 않은 브랜드입니다."));

        if (file != null) {

            UUID uuid = UUID.randomUUID();
            String fileName = GOODS_DIR + uuid + "_" + file.getOriginalFilename();
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

    public BrandEntity addBrand(final BrandDTO brandDTO,
                                final String userIdx){
        userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(() -> new RuntimeException("관리자로 로그인을 진행해주세요."));

        BrandEntity newBrand = BrandEntity.builder()
            .brandIdx(brandDTO.getBrandIdx())
            .brandName(brandDTO.getBrandName())
            .build();
        return brandRepository.save(newBrand);
    }
    // 유효한 관리자 인덱스가 없는 경우(토큰 만료))

    public RamyunEntity updateGoods(final Long ramyunIdx,
                                    final RamyunDTO ramyunDTO,
                                    final MultipartFile file,
                                    final String userIdx) throws Exception {
        // 유효한 라면 인덱스가 없는 경우(url로 들어갔는데 해당되는 라면이 없는 경우 예외 처리)
        RamyunEntity ramyun = ramyunRepository.findById(ramyunIdx).orElseThrow(()-> new RuntimeException("해당 상품이 존재하지 않습니다."));

        // 유효한 관리자 인덱스가 없는 경우(토큰 만료)
        userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(() -> new RuntimeException("관리자로 로그인을 진행해주세요."));

        // 등록되지 않은 브랜드인 경우
        BrandEntity brand = brandRepository.findByBrandName(ramyunDTO.getBrandName()).orElseThrow(() -> new RuntimeException("등록되지 않은 브랜드입니다."));


        String fileUrl = "";
        if(file != null){

            UUID uuid = UUID.randomUUID();
            String fileName = GOODS_DIR + uuid + "_" + file.getOriginalFilename();
            fileUrl = "https://" + cloudfront + "/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        } else {
            fileUrl = ramyun.getRamyunImg();
        }

        RamyunEntity updatedGoods = RamyunEntity.builder()
            .ramyunIdx(ramyun.getRamyunIdx())
            .ramyunName(ramyunDTO.getRamyunName())
            .ramyunImg(fileUrl)
            .ramyunNa(ramyunDTO.getRamyunNa())
            .isCup(ramyunDTO.getIsCup())
            .noodle(ramyunDTO.getNoodle())
            .cooking(ramyunDTO.getCooking())
            .gram(ramyunDTO.getGram())
            .ramyunKcal(ramyunDTO.getRamyunKcal())
            .scoville(ramyunDTO.getScoville())
            .brand(brand)
            .build();
        return ramyunRepository.save(updatedGoods);
    }

    public RamyunEntity deleteGoods(final Long ramyunIdx, final String userIdx){
        // 유효한 관리자 인덱스가 없는 경우(토큰 만료)
        userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(() -> new RuntimeException("관리자로 로그인을 진행해주세요."));

        RamyunEntity ramyun = ramyunRepository.findById(ramyunIdx).orElseThrow(()-> new RuntimeException("해당 상품이 존재하지 않습니다."));
        ramyunRepository.delete(ramyun);
        return ramyun;
    }

    public BrandEntity deleteBrand(final Long brandIdx, final String userIdx){
        // 유효한 관리자 인덱스가 없는 경우(토큰 만료)
        userRepository.findByUserIdx(Long.valueOf(userIdx)).orElseThrow(() -> new RuntimeException("관리자로 로그인을 진행해주세요."));

        BrandEntity brand = brandRepository.findById(brandIdx).orElseThrow(()-> new RuntimeException("해당 브랜드가 존재하지 않습니다."));
        brandRepository.delete(brand);
        return brand;
    }
}



