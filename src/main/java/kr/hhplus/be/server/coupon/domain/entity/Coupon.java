package kr.hhplus.be.server.coupon.domain.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.coupon.domain.DiscountType;
import kr.hhplus.be.server.coupon.application.exception.CouponErrorCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String couponName;

    @Enumerated
    private DiscountType discountType;

    private int discountValue;

    private LocalDateTime validTo;

    private LocalDateTime validFrom;

    private int count;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Coupon(Long id, String couponName, DiscountType discountType, int discountValue, LocalDateTime validTo, LocalDateTime validFrom, int count, LocalDateTime createdAt, LocalDateTime updatedAt) {

        if (!StringUtils.hasText(couponName)) {
            throw new CustomException(CouponErrorCode.COUPON_NAME_REQUIRED);
        }
        if (discountType == null) {
            throw new CustomException(CouponErrorCode.DISCOUNT_TYPE_INFORMATION_REQUIRED);
        }
        if (discountValue <= 0) {
            throw new CustomException(CouponErrorCode.DISCOUNT_VALUE_MUST_BE_POSITIVE);
        }
        if (validTo == null || validFrom == null) {
            throw new CustomException(CouponErrorCode.COUPON_VALID_PERIOD_REQUIRED);
        }
        if (count <= 0) {
            throw new CustomException(CouponErrorCode.COUPON_QUANTITY_MUST_BE_POSITIVE);
        }

        this.id = id;
        this.couponName = couponName;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.validTo = validTo;
        this.validFrom = validFrom;
        this.count = count;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}