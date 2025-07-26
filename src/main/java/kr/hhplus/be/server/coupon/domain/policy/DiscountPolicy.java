package kr.hhplus.be.server.coupon.domain.policy;

public interface DiscountPolicy {

    int calculateDiscount(int totalAmount);
}
