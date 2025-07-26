package kr.hhplus.be.server.product.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.product.domain.entity.PopularProduct;
import kr.hhplus.be.server.product.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResponse {

    @Schema(title = "상품 상세 정보 응답값")
    public record ProductDetailResponse(@Schema(description = "상품식별자", example = "1") long id,
                                     @Schema(description = "상품명", example = "상품명") String itemName,
                                     @Schema(description = "상품 가격", example = "1000") long price,
                                     @Schema(description = "상품 재고", example = "100") long stock) {

        public static ProductDetailResponse from(Product product) {
            return new ProductDetailResponse(product.getId(), product.getName(), product.getPrice(), product.getStock());
        }
    }

    @Schema(title = "인기 상품 상세 정보 응답값")
    public record PopularProductDetailResponse(@Schema(description = "상품식별자", example = "1") long id,
                                            @Schema(description = "상품 가격", example = "1000") long price,
                                            @Schema(description = "총 주문 수량", example = "100") long orderCount) {

        public static PopularProductDetailResponse from(PopularProduct popularProduct) {
            return new PopularProductDetailResponse(popularProduct.getId(), popularProduct.getPrice(), popularProduct.getOrderCount());
        }
    }
}