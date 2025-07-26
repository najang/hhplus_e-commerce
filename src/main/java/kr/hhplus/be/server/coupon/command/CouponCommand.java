package kr.hhplus.be.server.coupon.command;

import kr.hhplus.be.server.order.domain.entity.Order;

public class CouponCommand {

    public record CouponApplyCommand(
            Order order,
            long couponId
    ) {

        public static CouponApplyCommand of(Order order, long couponId) {
            return new CouponApplyCommand(order, couponId);
        }

        public long getUserId() {
            return order.getUserId();
        }

        public int getTotalAmount() {
            return order.getOrderAmountInfo().getTotalAmount();
        }
    }
}
