package kr.hhplus.be.server.interfaces.order.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {
    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;
    private Long couponId;

    @Valid
    @NotEmpty(message = "상품 목록은 1개 이상이여야 합니다.")
    private List<OrderProductRequest> products;

    @Builder
    private OrderRequest(Long userId, Long couponId, List<OrderProductRequest> products) {
        this.userId = userId;
        this.couponId = couponId;
        this.products = products;
    }

    public static OrderRequest of(Long userId, Long couponId, List<OrderProductRequest> products) {
        return new OrderRequest(userId, couponId, products);
    }
}
