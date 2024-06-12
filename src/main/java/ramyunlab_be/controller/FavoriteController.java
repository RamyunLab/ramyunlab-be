package ramyunlab_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ramyunlab_be.dto.FavoriteDTO;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.entity.FavoriteEntity;
import ramyunlab_be.service.FavoriteService;
import ramyunlab_be.vo.StatusCode;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
@Tag(name="Favorite", description = "찜 기능 관련 API")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @Operation(summary = "찜 목록 호출", description = "마이페이지의 찜목록에서 내가 찜한 라면들을 조회.(페이지네이션)<br />" +
            "pageable.pageNumber :: 현재 페이지 번호. +1해서 쓰면 됨.<br />" +
            "pageable.pageSize   :: 한 페이지에 들어가는 아이템 갯수.<br />" +
            "pageable.offset     :: 현재 페이지 첫번째 아이템의 인덱스 번호.<br />" +
            "totalPages :: 전체 페이지 번호.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "찜 목록 호출 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("")
    public ResponseEntity<ResDTO> getFavoriteList(@Parameter(name = "page", description = "현재 페이지 번호", in = ParameterIn.QUERY, example = "1")
                                                    @RequestParam(name = "page", required = false) Integer pageNo) {
        if(pageNo == null) pageNo = 1;
        Page<FavoriteDTO> favList = favoriteService.getFavoriteList(pageNo);

        return ResponseEntity.ok().body(ResDTO.builder()
                .statusCode(StatusCode.OK)
                .message("찜목록 호출 완료")
                .data(favList)
                .build());
    }
}
