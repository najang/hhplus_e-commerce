package kr.hhplus.be.server.interfaces.order.api;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderResponse {

        Long orderId;
        BigDecimal totalPrice;
        BigDecimal discountPrice;
        String orderedAt;

        @Builder
        private OrderResponse(Long orderId, BigDecimal totalPrice, BigDecimal discountPrice, String orderedAt) {
            this.orderId = orderId;
            this.totalPrice = totalPrice;
            this.discountPrice = discountPrice;
            this.orderedAt = orderedAt;
        }

        public static OrderResponse of(Long orderId, BigDecimal totalPrice, BigDecimal discountPrice, String orderedAt) {
            return new OrderResponse(orderId, totalPrice, discountPrice, orderedAt);
        }
}
