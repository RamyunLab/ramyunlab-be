package ramyunlab_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ramyunlab_be.entity.FavoriteEntity;
import ramyunlab_be.entity.RamyunEntity;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity,Long>, CrudRepository<FavoriteEntity,Long> {
}
