package kr.hhplus.be.server.interfaces.coupon.api;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponIssueResponse {
    private Long couponId;
    private Long userId;
    private String issuedAt;
    private String expiredAt;

    @Builder
    private CouponIssueResponse(Long couponId, Long userId, String issuedAt, String expiredAt) {
        this.couponId = couponId;
        this.userId = userId;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
    }

    public static CouponIssueResponse of(Long couponId, Long userId, String issuedAt, String expiredAt) {
        return new CouponIssueResponse(couponId, userId, issuedAt, expiredAt);
    }
}