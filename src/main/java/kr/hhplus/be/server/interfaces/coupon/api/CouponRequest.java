package kr.hhplus.be.server.interfaces.coupon.api;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponRequest {

    @NotNull(message = "쿠폰 ID는 필수입니다.")
    private Long couponId;

    @NotNull(message = "유저 ID는 필수입니다.")
    private Long userId;

    @Builder
    private CouponRequest(Long couponId) {
        this.couponId = couponId;
    }
}