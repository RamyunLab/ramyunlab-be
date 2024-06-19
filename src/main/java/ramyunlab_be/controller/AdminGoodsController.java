package ramyunlab_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ramyunlab_be.dto.*;
import ramyunlab_be.entity.BrandEntity;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.service.AdminGoodsService;
import ramyunlab_be.vo.StatusCode;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/admin", produces="application/json; charset=utf8")
@Tag(name = "AdminGoods", description = "관리자 관련 API")
public class AdminGoodsController {

    final private AdminGoodsService adminGoodsService;

    @Autowired
    public AdminGoodsController(final AdminGoodsService adminGoodsService){
        this.adminGoodsService = adminGoodsService;
    }


    @Operation(summary = "전체 상품 리스트 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "전체 상품 리스트 조회 성공"),
        @ApiResponse(responseCode = "400", description = "전체 상품 리스트 조회 실패")
    })
    @GetMapping("/goods")
    public ResponseEntity<ResDTO> getGoodsList(Pageable pageable){
        Page<RamyunProjection> results = AdminGoodsService.getGoodsList(pageable);

        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .message("전체 상품 리스트 조회 성공")
            .data(results)
            .build());
    }


    @Operation(summary = "선택된 상품 조회", description = "라면 인덱스, 토큰필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "선택된 상품 조회 성공"),
        @ApiResponse(responseCode = "400", description = "선택된 상품 조회 실패")
    })
    @GetMapping("/goods/{ramyunIdx}")
    public ResponseEntity<ResDTO> getGoods(@PathVariable Long ramyunIdx,
                                           @AuthenticationPrincipal String userIdx){
        log.warn("controller : {}", ramyunIdx);
        RamyunProjection result = adminGoodsService.getGoods(ramyunIdx, userIdx);
        log.warn("result : {}", result);
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .message("선택된 상품 조회 성공")
           .data(result)
           .build());
    }

    @Operation(summary = "상품 검색 조회", description = "keyword로 상품 검색, 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 검색 조회 성공"),
        @ApiResponse(responseCode = "400", description = "상품 검색 조회 실패")
    })
    @GetMapping("/searchGoods")
    public ResponseEntity<ResDTO> searchGoods(@RequestParam(required = false) String keyword,
                                             @AuthenticationPrincipal String userIdx) {
        List<RamyunProjection> results = adminGoodsService.searchGoods(keyword, userIdx);
        log.warn("controller : {}", keyword);
        log.warn("controller results : {}", results);
        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .data(results)
            .message("검색한 상품 목록 호출 완료")
            .build());
    }


    @Operation(summary = "상품 추가", description = "라면 모든 정보, 사진 파일, 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 추가 성공"),
        @ApiResponse(responseCode = "400", description = "상품 추가 실패")
    })
    @PostMapping("/goods")
    public ResponseEntity<ResDTO> addGoods(@RequestPart(value = "ramyunDTO") RamyunDTO ramyunDTO,
                                           @RequestPart(value = "file", required = false)MultipartFile file,
                                           @AuthenticationPrincipal String userIdx) throws Exception{
        log.warn("/admin/goods : {}", ramyunDTO);
        RamyunEntity addedRamyun = adminGoodsService.addGoods(ramyunDTO, file, userIdx);

        RamyunDTO responseRamyunDTO = RamyunDTO.builder()
            .ramyunIdx(addedRamyun.getRamyunIdx())
            .ramyunName(addedRamyun.getRamyunName())
            .ramyunImg(addedRamyun.getRamyunImg())
            .ramyunKcal(addedRamyun.getRamyunKcal())
            .noodle(addedRamyun.getNoodle())
            .isCup(addedRamyun.getIsCup())
            .cooking(addedRamyun.getCooking())
            .gram(addedRamyun.getGram())
            .ramyunNa(addedRamyun.getRamyunNa())
            .brandName(addedRamyun.getBrand().getBrandName())
            .build();

        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .data(responseRamyunDTO)
            .message("상품 추가 성공")
            .build());
    }

    @Operation(summary = "브랜드 추가", description = "브랜드명, 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "브랜드 추가 성공"),
        @ApiResponse(responseCode = "400", description = "브랜드 추가 실패")
    })
    @PostMapping("/brand")
    public ResponseEntity<ResDTO> addBrand(@RequestBody BrandDTO brandDTO,
                                           @AuthenticationPrincipal String userIdx){
        log.warn("brand : {}", brandDTO.getBrandIdx());
        BrandEntity addedBrand = adminGoodsService.addBrand(brandDTO, userIdx);

        BrandDTO responseBrandDTO = BrandDTO.builder()
            .brandIdx(addedBrand.getBrandIdx())
            .brandName(addedBrand.getBrandName())
            .build();

        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .data(responseBrandDTO)
            .message("브랜드 추가 성공")
            .build());
    }

    @Operation(summary = "상품 수정", description = "라면 모든 정보, 사진, 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
        @ApiResponse(responseCode = "400", description = "상품 수정 실패")
    })
    @PatchMapping("/goods/{ramyunIdx}")
    public ResponseEntity<ResDTO> updateGoods(@PathVariable Long ramyunIdx,
                                              @RequestPart(value = "ramyunDTO") RamyunDTO ramyunDTO,
                                              @RequestPart(value = "file") MultipartFile file,
                                              @AuthenticationPrincipal String userIdx) throws Exception{
        RamyunEntity updatedRamyun = adminGoodsService.updateGoods(ramyunIdx, ramyunDTO, file, userIdx);

        RamyunDTO responseRamyunDTO = RamyunDTO.builder()
            .ramyunIdx(updatedRamyun.getRamyunIdx())
            .ramyunName(updatedRamyun.getRamyunName())
            .ramyunImg(updatedRamyun.getRamyunImg())
            .ramyunKcal(updatedRamyun.getRamyunKcal())
            .noodle(updatedRamyun.getNoodle())
            .isCup(updatedRamyun.getIsCup())
            .cooking(updatedRamyun.getCooking())
            .gram(updatedRamyun.getGram())
            .ramyunNa(updatedRamyun.getRamyunNa())
            .brandName(updatedRamyun.getBrand().getBrandName())
            .build();

        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .data(responseRamyunDTO)
            .message("상품 수정 성공")
            .build());
    }

    @Operation(summary = "상품 삭제", description = "상품 idx, 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "상품 삭제 실패")
    })
    @DeleteMapping("/goods/{ramyunIdx}")
    public ResponseEntity<ResDTO> deleteGoods(@PathVariable Long ramyunIdx,
                                              @AuthenticationPrincipal String userIdx){
        adminGoodsService.deleteGoods(ramyunIdx, userIdx);
        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .message("상품 삭제 성공")
            .build());

    }

    @Operation(summary = "브랜드 삭제", description = "브랜드 idx, 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "브랜드 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "브랜드 삭제 실패")
    })
    @DeleteMapping("/brand/{brandIdx}")
    public ResponseEntity<ResDTO> deleteBrand(@PathVariable Long brandIdx,
                                              @AuthenticationPrincipal String userIdx){
        adminGoodsService.deleteBrand(brandIdx, userIdx);
        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .message("브랜드 삭제 성공")
            .build());
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResDTO> handleValidationException(ValidationException e) {
        return ResponseEntity
            .badRequest()
            .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message(e.getMessage()).build());
    }
}

