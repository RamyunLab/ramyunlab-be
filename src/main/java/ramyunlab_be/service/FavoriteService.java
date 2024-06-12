package ramyunlab_be.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ramyunlab_be.dto.FavoriteDTO;
import ramyunlab_be.entity.FavoriteEntity;
import ramyunlab_be.repository.FavoriteRepository;
import ramyunlab_be.vo.Pagenation;

@Slf4j
@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    public Page<FavoriteDTO> getFavoriteList(int pageNo, String userIdx) {

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
}
