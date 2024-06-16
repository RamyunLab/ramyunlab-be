package ramyunlab_be.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ramyunlab_be.dto.FavoriteDTO;
import ramyunlab_be.entity.FavoriteEntity;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.repository.FavoriteRepository;
import ramyunlab_be.vo.Pagenation;

@Slf4j
@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public Page<FavoriteDTO> getFavoriteList(Integer pageNo, String userIdx) {

        PageRequest pageRequest = PageRequest.of(pageNo - 1, Pagenation.PAGE_SIZE, Sort.by(Sort.Direction.DESC, "favCreatedAt"));

        Page<FavoriteEntity> result = favoriteRepository.findByUser_UserIdx(pageRequest, Long.valueOf(userIdx));

        return result.map(this::convert);
    }

    private FavoriteDTO convert(FavoriteEntity favoriteEntity) {
        return FavoriteDTO.builder()
                .favIdx(favoriteEntity.getFavIdx())
                .userIdx(favoriteEntity.getUser().getUserIdx())
                .ramyunIdx(favoriteEntity.getRamyun().getRamyunIdx())
                .ramyunImg(favoriteEntity.getRamyun().getRamyunImg())
                .ramyunName(favoriteEntity.getRamyun().getRamyunName())
                .favCreatedAt(favoriteEntity.getFavCreatedAt())
                .avgRate(favoriteEntity.getAvgRate())
                .reviewCount(favoriteEntity.getReviewCount())
                .build();
    }

    /* 찜 여부 조회 */
    public boolean isLiked(Long userIdx, Long ramyunIdx){
        boolean isLiked = false;
        Optional<FavoriteEntity> favorite = favoriteRepository.findLikedRamyun(userIdx, ramyunIdx);
        if(favorite.isPresent()) isLiked = true;
        return isLiked;
    }

    /* 찜 추가 */
    public void addFavorite(Long userIdx, Long ramyunIdx) {
        try {
            // 찜 추가 여부 확인
            Optional<FavoriteEntity> favorite = favoriteRepository.findLikedRamyun(userIdx, ramyunIdx);
            if(favorite.isPresent()) {
                log.info("FAVORITE IS PRESENT?? {}",favorite.get());
                throw new RuntimeException("중복된 요청입니다.");
            }else{
                FavoriteEntity fv = FavoriteEntity.builder()
                                                        .user(UserEntity.builder().userIdx(userIdx).build())
                                                        .ramyun(RamyunEntity.builder().ramyunIdx(ramyunIdx).build())
                                                        .build();
                FavoriteEntity result = favoriteRepository.save(fv);
                log.info("찜 추가 성공? {}", result);
            }
        } catch(Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("찜을 추가하지 못했습니다.");
        }
    }

    /* 찜 삭제 */
    public void deleteFavorite(Long userIdx, Long ramyunIdx) {
        try {
            // 찜 추가 여부 확인
//            Optional<FavoriteEntity> result = favoriteRepository.findLikedRamyun(userIdx, ramyunIdx);
            FavoriteEntity favorite = favoriteRepository.findLikedRamyun(userIdx, ramyunIdx)
                                                      .orElseThrow(()->new RuntimeException("찜한 내용을 찾을 수 없습니다."));
            log.info("IS FAVORITE EXIST {}", favorite);
            favoriteRepository.delete(favorite);
        }catch(Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
