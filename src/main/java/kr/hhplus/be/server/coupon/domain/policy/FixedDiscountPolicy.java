package kr.hhplus.be.server.coupon.domain.policy;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.coupon.exception.CouponErrorCode;

public class FixedDiscountPolicy implements DiscountPolicy {

    private final int discountAmount;

    public FixedDiscountPolicy(int discountAmount) {

        if (discountAmount < 0) {
            throw new CustomException(CouponErrorCode.INVALID_NEGATIVE_DISCOUNT_AMOUNT);
        }

        this.discountAmount = discountAmount;
    }

    @Override
    public int calculateDiscount(int totalAmount) {
        return Math.min(totalAmount, discountAmount);
    }
}