package kr.hhplus.be.server.interfaces.product.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductRequest {

    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    @NotNull(message = "상품 구매 수량은 필수입니다.")
    @Positive(message = "상품 구매 수량은 양수여야 합니다.")
    private Integer quantity;

    private ProductRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static ProductRequest of(Long productId, Integer quantity) {
        return new ProductRequest(productId, quantity);
    }
}