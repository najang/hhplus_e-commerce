package kr.hhplus.be.server.interfaces.coupon.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Coupon", description = "쿠폰 관리")
public interface CouponApiSpec {
    @Operation(summary = "유저 보유 쿠폰 조회",
            description = "유저가 보유한 쿠폰의 상세 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보유 쿠폰 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserCouponsResponse.class)))
    })
    ResponseEntity<List<UserCouponsResponse>> getUserCoupons(@RequestParam Long id);

    @Operation(summary = "선착순 쿠폰 발급",
            description = "선착순으로 유저에게 쿠폰을 발급한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 발급 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponIssueResponse.class)))
    })
    ResponseEntity<CouponIssueResponse> issueCoupon(@RequestBody CouponRequest request);
}
