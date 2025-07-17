package kr.hhplus.be.server.interfaces.coupon.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupon")
public class CouponApiController implements CouponApiSpec {

    @Override
    @GetMapping("/user/{id}")
    public ResponseEntity<List<UserCouponsResponse>> getUserCoupons(Long id) {
        return ResponseEntity.ok(
            List.of(
                UserCouponsResponse.of(1L, "웰컴 쿠폰", "2025-12-31 23:59:59", false),
                UserCouponsResponse.of(2L, "10% 할인 쿠폰", "2025-08-01 00:00:00", true)
            )
        );
    }

    @Override
    @PostMapping("/issue")
    public ResponseEntity<CouponIssueResponse> issueCoupon(CouponRequest request) {
        return ResponseEntity.ok(
                CouponIssueResponse.of(1L, 100L, "2025-12-31 23:59:59", "2025-12-31 23:59:59")
        );
    }
}
