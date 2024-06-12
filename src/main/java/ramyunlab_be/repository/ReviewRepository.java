package ramyunlab_be.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ramyunlab_be.entity.FavoriteEntity;
import ramyunlab_be.entity.ReviewEntity;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Page<ReviewEntity> findByUser_UserIdx(Pageable pageable, Long userIdx);
}
