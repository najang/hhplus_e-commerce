package kr.hhplus.be.server.coupon.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;


public class CouponRequest {

    @Schema(title = "쿠폰 발급 요청값")
    public record CouponIssueRequest(
            @Schema(description = "유저식별자", example = "1")
            @Positive
            long userId,
            @Schema(description = "쿠폰식별자", example = "1")
            @Positive
            long couponId
    ) {
    }
}