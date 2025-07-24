package kr.hhplus.be.server.product.interfaces.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.product.application.dto.request.ProductRequest;
import kr.hhplus.be.server.product.application.dto.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Product", description = "상품 관리")
public interface ProductApiSpec
{

    @Operation(summary = "상품 조회",
            description = "상품에 대한 상세 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class)))
    })
    ResponseEntity<ProductResponse.ProductDetailResponse> getProduct(@RequestParam @Positive Long productId);

    @Operation(summary = "인기 상품 조회",
            description = "데이터 플랫폼에서 최근 3일간 인기 상품을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인기 상품 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class)))
    })
    ResponseEntity<List<ProductResponse.PopularProductDetailResponse>> getPopularProducts();
}
