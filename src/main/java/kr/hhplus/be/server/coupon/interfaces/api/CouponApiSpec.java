package kr.hhplus.be.server.coupon.interfaces.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.coupon.application.dto.request.CouponRequest;
import kr.hhplus.be.server.coupon.application.dto.response.CouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Coupon", description = "쿠폰 관리")
public interface CouponApiSpec {
    @Operation(summary = "유저 보유 쿠폰 조회",
            description = "유저가 보유한 쿠폰의 상세 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보유 쿠폰 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponResponse.UserCouponResponse.class)))
    })
    ResponseEntity<List<CouponResponse.UserCouponResponse>> getUserCoupons(@RequestParam Long userId);

    @Operation(summary = "선착순 쿠폰 발급",
            description = "선착순으로 유저에게 쿠폰을 발급한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 발급 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponResponse.UserCouponResponse.class)))
    })
    ResponseEntity<CouponResponse.UserCouponResponse> issueCoupon(@RequestBody CouponRequest.CouponIssueRequest request);
}
