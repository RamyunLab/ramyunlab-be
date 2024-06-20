package ramyunlab_be.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.dto.RamyunFilterDTO;

import static ramyunlab_be.entity.QRamyunEntity.ramyunEntity;
import static ramyunlab_be.entity.QReviewEntity.reviewEntity;
import static ramyunlab_be.entity.QFavoriteEntity.favoriteEntity;

@AllArgsConstructor
@Slf4j
@Repository
public class RamyunCustomRepositoryImpl implements RamyunCustomRepository{

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public RamyunDTO getRamyunInfo (Long ramyunIdx) {

    JPAQuery<RamyunDTO> query = jpaQueryFactory
        .select(Projections.fields(
            RamyunDTO.class,
            ramyunEntity.ramyunIdx,
            ramyunEntity.ramyunName,
            ramyunEntity.ramyunImg,
            ramyunEntity.brand.brandName,
            ramyunEntity.noodle,
            ramyunEntity.ramyunKcal,
            ramyunEntity.isCup,
            ramyunEntity.cooking,
            ramyunEntity.gram,
            ramyunEntity.ramyunNa,
            ramyunEntity.scoville,
            avgRate().as("avgRate"),
            reviewCount().as("reviewCount")))
        .from(ramyunEntity)
        .leftJoin(reviewEntity).on(ramyunEntity.ramyunIdx.eq(reviewEntity.ramyun.ramyunIdx))
        .where(ramyunEntity.ramyunIdx.eq(ramyunIdx)
                                     .and(reviewEntity.rvDeletedAt.isNull()))
        .groupBy(ramyunEntity.ramyunIdx);

    return query.fetchOne();
  }

  @Override
  public Page<RamyunDTO> getRamyunList (Pageable pageable, String sort, String direction, RamyunFilterDTO filterDTO, Long userIdx){

     // Sorting 정보 추가
    OrderSpecifier<?> orderSpecifier;
    switch (sort != null ? sort : "default") {
      case "name":
        if(direction == null) {direction = "asc";}
        orderSpecifier = direction.equals("desc") ? ramyunEntity.ramyunName.desc() : ramyunEntity.ramyunName.asc();
        break;
      case "avgRate":
        orderSpecifier = avgRate().desc();
        break;
      case "reviewCount":
        orderSpecifier = reviewCount().desc();
        break;
      default:
        orderSpecifier = ramyunEntity.ramyunName.asc();
    }

    JPAQuery<RamyunDTO> query = jpaQueryFactory
        .select(Projections.fields(
            RamyunDTO.class,
            ramyunEntity.ramyunIdx,
            ramyunEntity.ramyunName,
            ramyunEntity.ramyunImg,
            ramyunEntity.brand.brandName,
            ramyunEntity.noodle,
            ramyunEntity.ramyunKcal,
            ramyunEntity.isCup,
            ramyunEntity.cooking,
            ramyunEntity.gram,
            ramyunEntity.ramyunNa,
            ramyunEntity.scoville,
            avgRate().as("avgRate"),
            reviewCount().as("reviewCount")
            ,ExpressionUtils.as(isLikeExist(userIdx), "isLiked")
           ))
                   .from(ramyunEntity)
                   .leftJoin(reviewEntity).on(ramyunEntity.ramyunIdx.eq(reviewEntity.ramyun.ramyunIdx))
                   .where(filterConditions(filterDTO)
                              .and(reviewEntity.rvDeletedAt.isNull()))
                   .groupBy(ramyunEntity.ramyunIdx)
                   .orderBy(orderSpecifier);

    // 총 데이터 개수
    long total = query.fetch().size();
    log.info("총 개수 {}", total);

    // 페이지네이션 적용
    List<RamyunDTO> results = query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    return new PageImpl<>(results, pageable, total);
  }


  
  /* 집계함수 */
  // 평점 계산
  private NumberExpression<Double> avgRate(){
    return MathExpressions.round(reviewEntity.rate.avg().coalesce(0.0),2);
  }

  // 리뷰 개수
  private NumberExpression<Long> reviewCount(){
    return reviewEntity.rate.count().coalesce(0L);
  }


  /* 찜 목록 조회 */
  private BooleanExpression isLikeExist(Long userIdx){
    log.info("useridx {}", userIdx);
    if (userIdx == null){ return Expressions.asBoolean(false); }
    return new CaseBuilder()
        .when(
            JPAExpressions.select(favoriteEntity.favIdx.count())
                          .from(favoriteEntity)
                          .where(favoriteEntity.ramyun.ramyunIdx.eq(ramyunEntity.ramyunIdx)
                                                                .and(favoriteEntity.user.userIdx.eq(userIdx)))
                          .gt(0L)
             )
        .then(true)
        .otherwise(false);
  }


  /* 조회 조건 */
  private BooleanBuilder filterConditions(RamyunFilterDTO conditions){
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(ramyunNotDeleted())
        .and(ramyunNameContains(conditions.getName()))
        .and(ramyunBrandContains(conditions.getBrand()))
        .and(noodleContains(conditions.getNoodle()))
        .and(ramyunIsCup(conditions.getIsCup()))
        .and(ramyunCooking(conditions.getCooking()))
        .and(ramyunGramsRange(conditions.getGram()))
        .and(ramyunKcalRange(conditions.getKcal()))
        .and(ramyunNaRange(conditions.getNa()));
    return builder;
  }

  // 삭제된 라면 제외
  private BooleanExpression ramyunNotDeleted(){
    return ramyunEntity.ramyunDeletedAt.isNull();
  }

  // 라면 이름
  private BooleanExpression ramyunNameContains(String name){
    return StringUtils.hasText(name) ? ramyunEntity.ramyunName.contains(name) : null;
  }

  // 라면 브랜드
  private BooleanExpression ramyunBrandContains(List<Long> brandList){
    BooleanExpression query = null;
    BooleanExpression newQuery;

    if(brandList == null){
      return null;
    }
    for(Long brandIdx : brandList){
      newQuery = ramyunEntity.brand.brandIdx.eq(brandIdx);
      if(query == null){
        query = newQuery;
      }else{
        if(newQuery != null) query = query.or(newQuery);
      }
    }
    log.info("query:: {}", query);
    return query;
  }

  // 면 종류
  private BooleanExpression noodleContains(List<Boolean> noodles){
    if(noodles == null || noodles.size() == 2){
      return null;
    }
    return ramyunEntity.noodle.eq(noodles.get(0));
  }

  // 컵라면 여부
  private BooleanExpression ramyunIsCup(List<Boolean> isCup){
    if(isCup == null || isCup.size() == 2){
      return null;
    }
    return ramyunEntity.isCup.eq(isCup.get(0));
  }

  // 조리 방식
  private BooleanExpression ramyunCooking(List<Boolean> cooking){
    if(cooking == null || cooking.size() == 2){
      return null;
    }
    return ramyunEntity.cooking.eq(cooking.get(0));
  }

  // 중량 범위
  private BooleanExpression ramyunGramsRange(List<Integer> grams){
    BooleanExpression query = null;

    if(grams == null || grams.size() == 2){
      log.info("그램 조건 없음!");
      return null;
    }
    switch (grams.get(0)) {
      case 1:
        return query = ramyunEntity.gram.loe(100);
      case 2:
        return query = ramyunEntity.gram.goe(100);
      default:
        break;
    };
    log.info("gram QUERY ::: {}", query);
    return query;
  }


  // 칼로리 범위
  private BooleanExpression ramyunKcalRange(List<Integer> kcals){
    BooleanExpression query = null;
    BooleanExpression newQuery;
    if(kcals == null || kcals.size() == 3){
      return null;
    }
    for(Integer kcal : kcals){
      switch (kcal) {
        case 1:
          newQuery = ramyunEntity.ramyunKcal.loe(300);
          break;
        case 2:
          newQuery = ramyunEntity.ramyunKcal.between(300, 500);
          break;
        case 3:
          newQuery = ramyunEntity.ramyunKcal.goe(500);
          break;
        default:
          newQuery = null;
          break;
      };
      if(query != null){
        if(newQuery != null) query = query.or(newQuery);
      }else{
        query = newQuery;
      }
    }
    log.info("KCAL QUERY::: {}", query);
    return query;
  }

  // 나트륨 범위
  private BooleanExpression ramyunNaRange (List<Integer> nas){
    BooleanExpression query = null;

    if(nas == null || nas.size() == 4){
      return null;
    }

    for(Integer na : nas){
      BooleanExpression newQuery = switch (na) {
        case 1 -> ramyunEntity.ramyunNa.loe(1000);
        case 2 -> ramyunEntity.ramyunNa.between(1000, 1400);
        case 3 -> ramyunEntity.ramyunNa.between(1400, 1700);
        case 4 -> ramyunEntity.ramyunNa.goe(1700);
        default -> null;
      };
      if(query != null){
        if(newQuery != null) query = query.or(newQuery);
      }else{
        query = newQuery;
      }
    }
    log.info("query:: {}", query);
    return query;
  }

  // NPE 예외 처리
  private BooleanBuilder nullSafeBooleanBuilder(Supplier<BooleanExpression> supplier){
    try{
      return new BooleanBuilder(supplier.get());
    }catch(IllegalArgumentException e){
      return new BooleanBuilder();
    }
  }
}
