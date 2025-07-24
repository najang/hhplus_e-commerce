package kr.hhplus.be.server.coupon.presentation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.coupon.application.dto.request.CouponRequest;
import kr.hhplus.be.server.coupon.application.dto.response.CouponResponse;
import kr.hhplus.be.server.coupon.application.service.CouponService;
import kr.hhplus.be.server.coupon.interfaces.api.CouponApiSpec;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupon")
public class CouponController implements CouponApiSpec {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @Override
    @GetMapping("/user/{id}")
    public ResponseEntity<List<CouponResponse.UserCouponResponse>> getUserCoupons(@RequestParam @Positive Long userId) {

        List<CouponResponse.UserCouponResponse> response = couponService.findByUserId(userId).stream()
                .map(CouponResponse.UserCouponResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/issue")
    public ResponseEntity<CouponResponse.UserCouponResponse> issueCoupon(@RequestBody @Valid CouponRequest.CouponIssueRequest request) {
        return ResponseEntity.ok(new CouponResponse.UserCouponResponse(1L, "쿠폰1", "2025-12-31", false));
    }
}
