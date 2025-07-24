package kr.hhplus.be.server.order.application.dto.result;

import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.order.domain.entity.OrderInfo;
import kr.hhplus.be.server.order.domain.entity.OrderProduct;
import kr.hhplus.be.server.order.domain.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResult {

    public record OrderCreateResult(
            long orderId,
            OrderStatus orderStatus,
            Long couponId,
            List<OrderProductCreateResult> orderProducts,
            long totalAmount,
            long productTotalAmount,
            long discountAmount,
            LocalDateTime createdAt
    ) {

        public static OrderCreateResult from(OrderInfo orderInfo) {

            List<OrderProductCreateResult> productResults = orderInfo.orderProducts().stream()
                    .map(OrderProductCreateResult::from)
                    .toList();

            Order order = orderInfo.order();

            return new OrderCreateResult(
                    order.getId(),
                    order.getOrderStatus(),
                    null,
                    productResults,
                    order.getOrderAmountInfo().getTotalAmount(),
                    order.getOrderAmountInfo().getProductTotalAmount(),
                    order.getOrderAmountInfo().getDiscountAmount(),
                    order.getCreatedAt()
            );
        }
    }

    public record OrderProductCreateResult(
            long orderProductId,
            String orderProductName,
            long sellPrice,
            int count
    ) {

        public static OrderProductCreateResult from(OrderProduct orderProduct) {
            return new OrderProductCreateResult(orderProduct.getId(), orderProduct.getProductName(), orderProduct.getSellPrice(), orderProduct.getCount());
        }
    }
}
