package kr.hhplus.be.server.coupon.infrastructure;

import kr.hhplus.be.server.coupon.domain.entity.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue, Long> {
    List<CouponIssue> findByUserId(long userId);

    Optional<CouponIssue> findByUserIdAndCouponId(long userId, long couponId);

    boolean existByUserIdAndCouponId(long userId, long couponId);
}
