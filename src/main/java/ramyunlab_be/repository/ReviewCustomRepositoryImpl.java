package ramyunlab_be.repository;

import static ramyunlab_be.entity.QRamyunEntity.ramyunEntity;
import static ramyunlab_be.entity.QReviewEntity.reviewEntity;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ramyunlab_be.dto.ReviewDTO;

import static ramyunlab_be.entity.QRecommendEntity.recommendEntity;

@Slf4j
@AllArgsConstructor
@Repository
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<ReviewDTO> findReviewByRamyunIdx (Long ramyunIdx, Long userIdx, Pageable pageable){
    JPAQuery<ReviewDTO> query = jpaQueryFactory
        .select(Projections.fields(
            ReviewDTO.class,
            reviewEntity.rvIdx,
            reviewEntity.reviewContent,
            reviewEntity.rate,
            reviewEntity.reviewPhotoUrl,
            reviewEntity.rvCreatedAt,
            reviewEntity.rvUpdatedAt,
            reviewEntity.ramyun.ramyunIdx,
            reviewEntity.user.userIdx,
            reviewEntity.user.nickname,
            reviewEntity.rvRecommendCount,
            reviewEntity.rvIsReported,
            ExpressionUtils.as(isRecommendExist(userIdx), "isRecommended")))
        .from(reviewEntity)
        .leftJoin(reviewEntity).on(ramyunEntity.ramyunIdx.eq(reviewEntity.ramyun.ramyunIdx))
        .where(reviewEntity.ramyun.ramyunIdx.eq(ramyunIdx)
                                            .and(reviewEntity.rvDeletedAt.isNull()))
        .orderBy(reviewEntity.rvCreatedAt.asc());

    long total = query.fetch().size();

    List<ReviewDTO> results = query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

    return new PageImpl<>(results, pageable, total);
  }


  public Optional<List<ReviewDTO>> findBestReviewByRamyunIdx (Long ramyunIdx, Long userIdx, Pageable pageable){
    JPAQuery<ReviewDTO> query = jpaQueryFactory
        .select(Projections.fields(
            ReviewDTO.class,
            reviewEntity.rvIdx,
            reviewEntity.reviewContent,
            reviewEntity.rate,
            reviewEntity.reviewPhotoUrl,
            reviewEntity.rvCreatedAt,
            reviewEntity.rvUpdatedAt,
            reviewEntity.ramyun.ramyunIdx,
            reviewEntity.user.userIdx,
            reviewEntity.user.nickname,
            reviewEntity.rvRecommendCount,
            reviewEntity.rvIsReported,
            ExpressionUtils.as(isRecommendExist(userIdx), "isRecommended")))
        .from(reviewEntity)
        .leftJoin(reviewEntity).on(ramyunEntity.ramyunIdx.eq(reviewEntity.ramyun.ramyunIdx))
        .where(reviewEntity.ramyun.ramyunIdx.eq(ramyunIdx)
                                            .and(reviewEntity.rvDeletedAt.isNull())
                                            .and(reviewEntity.rvRecommendCount.goe(10)))
        .orderBy(reviewEntity.rvRecommendCount.desc(), reviewEntity.rvCreatedAt.asc());

    List<ReviewDTO> results = query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

    return Optional.ofNullable(results.isEmpty() ? null : results);
  }


  /* 추천 여부 조회 */
  private BooleanExpression isRecommendExist (Long userIdx){
    if (userIdx == null){ return Expressions.asBoolean(false); }
    return new CaseBuilder()
        .when(
            JPAExpressions.select(recommendEntity.recommendIdx.count())
                          .from(recommendEntity)
                          .where(recommendEntity.review.rvIdx.eq(reviewEntity.rvIdx)
                                                             .and(recommendEntity.user.userIdx.eq(userIdx)))
                          .gt(0L)
             )
        .then(true)
        .otherwise(false);
  }
}