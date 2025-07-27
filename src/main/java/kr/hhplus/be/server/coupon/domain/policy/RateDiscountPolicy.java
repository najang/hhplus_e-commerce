package kr.hhplus.be.server.coupon.domain.policy;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.coupon.application.exception.CouponErrorCode;

public class RateDiscountPolicy implements DiscountPolicy {

    private final int discountRate;

    public RateDiscountPolicy(int discountRate) {

        if (discountRate < 0 || discountRate > 100) {
            throw new CustomException(CouponErrorCode.INVALID_DISCOUNT_RATE_RANGE);
        }

        this.discountRate = discountRate;
    }

    @Override
    public int calculateDiscount(int totalAmount) {
        return totalAmount * discountRate / 100;
    }
}
