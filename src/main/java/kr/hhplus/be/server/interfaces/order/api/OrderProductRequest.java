package kr.hhplus.be.server.interfaces.order.api;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderProductRequest {
    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    @NotNull(message = "수량은 필수입니다.")
    private BigDecimal quantity;

    @Builder
    private OrderProductRequest(Long productId, BigDecimal quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static OrderProductRequest of(Long productId, BigDecimal quantity) {
        return new OrderProductRequest(productId, quantity);
    }
}
