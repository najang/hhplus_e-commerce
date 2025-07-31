package kr.hhplus.be.server.coupon.domain.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.coupon.domain.DiscountType;
import kr.hhplus.be.server.coupon.domain.policy.DiscountPolicy;
import kr.hhplus.be.server.coupon.application.exception.CouponErrorCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"userId", "couponId"})
public class CouponIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long couponId;

    private String couponName;

    @Enumerated
    private DiscountType discountType;

    private int discountValue;

    private long userId;

    private LocalDateTime expiredAt;

    private boolean isUsed;

    @CreatedDate
    private LocalDateTime issuedAt;

    public static CouponIssue of(long userId, Coupon coupon) {
        return new CouponIssue(null, coupon.getId(), coupon.getCouponName(), coupon.getDiscountType(), coupon.getDiscountValue(), userId, coupon.getValidFrom(), false, LocalDateTime.now());
    }

    public CouponIssue(Long id, long couponId, String couponName, DiscountType discountType, int discountValue, long userId, LocalDateTime expiredAt, boolean isUsed, LocalDateTime issuedAt) {

        if(couponId < 0) {
            throw new CustomException(CouponErrorCode.INVALID_NEGATIVE_COUPON_ID);
        }
        if(!StringUtils.hasText(couponName)) {
            throw new CustomException(CouponErrorCode.COUPON_NAME_REQUIRED);
        }
        if(discountType == null) {
            throw new CustomException(CouponErrorCode.DISCOUNT_TYPE_INFORMATION_REQUIRED);
        }
        if(discountValue <= 0) {
            throw new CustomException(CouponErrorCode.DISCOUNT_VALUE_MUST_BE_POSITIVE);
        }
        if(userId < 0) {
            throw new CustomException(CouponErrorCode.INVALID_NEGATIVE_USER_ID);
        }
        if(expiredAt == null) {
            throw new CustomException(CouponErrorCode.EXPIRATION_DATETIME_REQUIRED);
        }

        this.id = id;
        this.couponId = couponId;
        this.couponName = couponName;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.userId = userId;
        this.expiredAt = expiredAt;
        this.isUsed = isUsed;
        this.issuedAt = issuedAt;
    }

    public int applyDiscount(int totalAmount) {

        if (isUsed || expiredAt.isBefore(LocalDateTime.now())) {
            throw new CustomException(CouponErrorCode.COUPON_NOT_USABLE);
        }
        this.isUsed = true;
        DiscountPolicy discountPolicy = discountType.getDiscountPolicy(discountValue);
        return discountPolicy.calculateDiscount(totalAmount);
    }
}