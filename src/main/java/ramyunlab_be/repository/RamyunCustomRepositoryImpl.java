package ramyunlab_be.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.entity.QRamyunEntity;
import ramyunlab_be.entity.QReviewEntity;

@AllArgsConstructor
@Repository
public class RamyunCustomRepositoryImpl implements RamyunCustomRepository{

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<RamyunDTO> findAllListQuery (Pageable pageable, String sort, String direction){
    QRamyunEntity ramyun = QRamyunEntity.ramyunEntity;
    QReviewEntity review = QReviewEntity.reviewEntity;

    NumberExpression<Double> avgRate = review.rate.avg().coalesce(0.0);
    NumberExpression<Long> reviewCount = review.rate.count().coalesce(0L);

    // Sorting 정보 추가
    OrderSpecifier<?> orderSpecifier;
    switch (sort != null ? sort : "default") {
      case "name":
        if(direction == null) {direction = "asc";}
        orderSpecifier = direction.equals("desc") ? ramyun.ramyunName.desc() : ramyun.ramyunName.asc();
        break;
      case "avgRate":
        orderSpecifier = avgRate.desc();
        break;
      case "reviewCount":
        orderSpecifier = reviewCount.desc();
        break;
      default:
        orderSpecifier = ramyun.ramyunName.asc();
    }

    JPAQuery<RamyunDTO> query = jpaQueryFactory
        .select(Projections.fields(
            RamyunDTO.class,
            ramyun.ramyunIdx,
            ramyun.ramyunName,
            ramyun.ramyunImg,
            ramyun.brand.brandName,
            ramyun.noodle,
            ramyun.ramyunKcal,
            ramyun.isCup,
            ramyun.cooking,
            ramyun.gram,
            ramyun.ramyunNa,
            ramyun.scoville,
            avgRate.as("avgRate"),
            reviewCount.as("reviewCount")))
        .from(ramyun)
        .leftJoin(review).on(ramyun.ramyunIdx.eq(review.ramyun.ramyunIdx))
        .groupBy(ramyun.ramyunIdx)
        .orderBy(orderSpecifier);

    // Apply pagination
    List<RamyunDTO> results = query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // Fetch the total count
    long total = jpaQueryFactory
        .select(ramyun.ramyunIdx.count())
        .from(ramyun)
        .leftJoin(review).on(ramyun.ramyunIdx.eq(review.ramyun.ramyunIdx))
        .groupBy(ramyun.ramyunIdx)
        .fetch().size();

    return new PageImpl<>(results, pageable, total);
  }
}
