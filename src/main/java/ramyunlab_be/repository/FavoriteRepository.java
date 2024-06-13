package ramyunlab_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ramyunlab_be.dto.FavoriteDTO;
import ramyunlab_be.entity.FavoriteEntity;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity,Long> {
    // @Query("SELECT f FROM FavoriteEntity f Where f.userIdx = :userIdx");
    Page<FavoriteEntity> findByUser_UserIdx(Pageable pageable, Long userIdx);

    // 찜 추가
    FavoriteEntity save (FavoriteDTO favorite);
}
