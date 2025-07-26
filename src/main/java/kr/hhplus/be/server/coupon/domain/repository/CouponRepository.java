package kr.hhplus.be.server.coupon.domain.repository;

import kr.hhplus.be.server.coupon.domain.entity.CouponIssue;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository {

    List<CouponIssue> findByUserId(long userId);

    Optional<CouponIssue> findByUserIdAndCouponId(long userId, long couponId);
}