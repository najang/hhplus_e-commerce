package kr.hhplus.be.server.order.domain.entity;

import java.util.List;

public record OrderInfo (
    Order order,
    List<OrderProduct> orderProducts
) {
    public static OrderInfo of(Order order, List<OrderProduct> orderProducts) {
        return new OrderInfo(order, orderProducts);
    }

    public int getTotalAmount() {
        return order.getOrderAmountInfo().getTotalAmount();
    }

    public void applyDiscount(int discountAmount) {
        order.applyDiscount(discountAmount);
    }
}
