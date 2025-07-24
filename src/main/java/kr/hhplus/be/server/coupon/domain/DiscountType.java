package kr.hhplus.be.server.coupon.domain;

import kr.hhplus.be.server.coupon.domain.policy.DiscountPolicy;
import kr.hhplus.be.server.coupon.domain.policy.FixedDiscountPolicy;
import kr.hhplus.be.server.coupon.domain.policy.RateDiscountPolicy;

public enum DiscountType {
    FIXED {
        @Override
        public DiscountPolicy getDiscountPolicy(int discountValue) {
            return new FixedDiscountPolicy(discountValue);
        }
    },
    RATE {
        @Override
        public DiscountPolicy getDiscountPolicy(int discountValue) {
            return new RateDiscountPolicy(discountValue);
        }
    };

    public abstract DiscountPolicy getDiscountPolicy(int discountValue);
    }
