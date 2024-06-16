package ramyunlab_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ValidationException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ramyunlab_be.dto.FavoriteDTO;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.service.FavoriteService;
import ramyunlab_be.vo.StatusCode;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name="Favorite", description = "찜 기능 관련 API")
public class FavoriteController {

    private FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @Operation(summary = "찜 목록 호출", description = "마이페이지의 찜목록에서 내가 찜한 라면들을 조회.(페이지네이션)<br />" +
            "pageable.pageNumber :: 현재 페이지 번호. +1해서 쓰면 됨.<br />" +
            "pageable.pageSize   :: 한 페이지에 들어가는 아이템 갯수.<br />" +
            "pageable.offset     :: 현재 페이지 첫번째 아이템의 인덱스 번호.<br />" +
            "totalPages :: 전체 페이지 번호.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "찜 목록 호출 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/user/favorite")
    public ResponseEntity<ResDTO<Object>> getFavoriteList(@Parameter(name = "page", description = "현재 페이지 번호", in = ParameterIn.QUERY, example = "1")
                                                    @RequestParam(name = "page", required = false) Integer pageNo,
                                                  @AuthenticationPrincipal String userIdx) {
        if(pageNo == null) pageNo = 1;
        Page<FavoriteDTO> favList = favoriteService.getFavoriteList(pageNo, userIdx);

        return ResponseEntity.ok().body(ResDTO.builder()
                .statusCode(StatusCode.OK)
                .message("찜목록 호출 완료")
                .data(favList)
                .build());
    }


    @Operation(summary = "찜 추가", description = "라면 찜 추가\n로그인 필수, RequestBody에 ramyunIdx 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "찜 추가 성공"),
        @ApiResponse(responseCode = "401", description = "찜 추가 실패 - 비로그인 상태"),
        @ApiResponse(responseCode = "400", description = "찜 추가 실패 - 그 외 예외사항 발생 시")
    })
    @PostMapping("/favorites")
    public ResponseEntity<ResDTO<Object>> addFavorite (@AuthenticationPrincipal String userIdx, @RequestBody Map<String,Long> ramyunIdx) {
        try {
            // 로그인 여부 판별
            if (userIdx == null && !userIdx.equals("anonymousUser")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResDTO.builder()
                                                 .statusCode(StatusCode.UNAUTHORIZED)
                                                 .message("로그인이 필요합니다.")
                                                 .build());
            }
            favoriteService.addFavorite(Long.valueOf(userIdx), ramyunIdx.get("ramyunIdx"));
            return ResponseEntity.ok().body(ResDTO.builder()
                                                  .statusCode(StatusCode.OK)
                                                  .message("찜 추가 성공")
                                                  .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                                 .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message("찜 추가 실패").build());
        }
    }

    @Operation(summary = "찜 삭제", description = "라면 찜 삭제\n로그인 필수, ramyunIdx 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "찜 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "찜 삭제 실패")
    })
    @DeleteMapping("/favorites")
    public ResponseEntity<ResDTO<Object>> deleteFavorite(@AuthenticationPrincipal String userIdx, @RequestBody Map<String, Long> ramyunIdx) {
        try {
            favoriteService.deleteFavorite(Long.valueOf(userIdx), ramyunIdx.get("ramyunIdx"));
            return ResponseEntity.ok().body(ResDTO.builder()
                                                      .statusCode(StatusCode.OK)
                                                      .message("찜 삭제 성공")
                                                      .build());
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(ResDTO.builder()
                                                          .statusCode(StatusCode.BAD_REQUEST)
                                                          .message(e.getMessage()).build());
        }
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResDTO<Object>> handleValidationException(ValidationException e) {
        return ResponseEntity
                .badRequest()
                .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message(e.getMessage()).build());
    }
}
