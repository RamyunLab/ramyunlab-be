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
                .build();
    }

    public Boolean addFavorite(FavoriteDTO dto) {
        try {
            FavoriteEntity favorite = FavoriteEntity.builder()
                                                    .user(UserEntity.builder().userIdx(dto.getUserIdx()).build())
                                                    .ramyun(RamyunEntity.builder().ramyunIdx(dto.getRamyunIdx()).build())
                                                    .build();
            FavoriteEntity result = favoriteRepository.save(favorite);
            log.info("찜 추가 성공? {}", result);
            return true;
        }catch(Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("찜을 추가하지 못했습니다.");
        }
    }

        public Boolean deleteFavorite(Long userIdx, Long ramyunIdx) {
            try {
                // 찜 추가 여부 확인
                Optional<FavoriteEntity> result = favoriteRepository.findLikedRamyun(userIdx, ramyunIdx);
                if(!result.isPresent()) {
                    throw new NoSuchElementException("찜한 내용을 찾을 수 없습니다.");
                }else{
                    FavoriteEntity favorite = result.get();
                    favoriteRepository.delete(favorite);
                }
                return true;
            }catch(Exception e){
                log.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }

}
