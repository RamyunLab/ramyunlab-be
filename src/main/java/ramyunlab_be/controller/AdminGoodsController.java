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
import ramyunlab_be.dto.BrandDTO;
import ramyunlab_be.dto.RamyunDTO;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.entity.BrandEntity;
import ramyunlab_be.entity.RamyunEntity;
import ramyunlab_be.service.AdminService;
import ramyunlab_be.vo.StatusCode;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/admin", produces="application/json; charset=utf8")
@Tag(name = "Admin", description = "관리자 관련 API")
public class AdminGoodsController {

    final private AdminService adminService;

    @Autowired
    public AdminGoodsController(final AdminService adminService){
        this.adminService = adminService;
    }


    @Operation(summary = "전체 상품 리스트 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "전체 상품 리스트 조회 성공"),
        @ApiResponse(responseCode = "400", description = "전체 상품 리스트 조회 실패")
    })
    @GetMapping("/goods")
    public ResponseEntity<ResDTO> getGoodsList(Pageable pageable){
        Page<RamyunEntity> results = AdminService.getGoodsList(pageable);
        List<RamyunEntity> responseResult = results.stream()
            .map(result -> RamyunEntity.builder()
                .ramyunIdx(result.getRamyunIdx())
                .ramyunName(result.getRamyunName())
                .ramyunImg(result.getRamyunImg())
                .ramyunKcal(result.getRamyunKcal())
                .noodle(result.getNoodle())
                .isCup(result.getIsCup())
                .cooking(result.getCooking())
                .gram(result.getGram())
                .ramyunNa(result.getRamyunNa())
                .build())
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .message("전체 상품 리스트 조회 성공")
            .data(responseResult)
            .build());
    }

    @Operation(summary = "상품 추가", description = "라면 모든 정보, 사진 파일, 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 추가 성공"),
        @ApiResponse(responseCode = "400", description = "상품 추가 실패")
    })
    @PostMapping("/goods")
    public ResponseEntity<ResDTO> addGoods(@RequestPart RamyunDTO ramyunDTO,
                                           @RequestPart(required = false)MultipartFile file,
                                           @AuthenticationPrincipal String userIdx) throws Exception{
        RamyunEntity addedRamyun = adminService.addGoods(ramyunDTO, file, userIdx);

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
        BrandEntity addedBrand = adminService.addBrand(brandDTO, userIdx);

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
                                              @RequestPart RamyunDTO ramyunDTO,
                                              @RequestPart(required = false)MultipartFile file,
                                              @AuthenticationPrincipal String userIdx) throws Exception{
        RamyunEntity updatedRamyun = adminService.updateGoods(ramyunIdx, ramyunDTO, file, userIdx);

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
        adminService.deleteGoods(ramyunIdx, userIdx);
        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .message("상품 삭제 성공")
            .build());

    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResDTO> handleValidationException(ValidationException e) {
        return ResponseEntity
            .badRequest()
            .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message(e.getMessage()).build());
    }
}

