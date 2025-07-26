package kr.hhplus.be.server.coupon.application.service;

import kr.hhplus.be.server.coupon.command.CouponCommand;
import kr.hhplus.be.server.coupon.domain.entity.CouponIssue;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<CouponIssue> findByUserId(long userId) {
        return couponRepository.findByUserId(userId);
    }

    @Transactional
    public int applyCoupon(CouponCommand.CouponApplyCommand command) {

        CouponIssue couponIssue = couponRepository.findByUserIdAndCouponId(command.getUserId(), command.couponId())
                .orElseThrow(() -> new RuntimeException("해당 쿠폰을 보유하고 있지 않습니다."));

        return couponIssue.applyDiscount(command.getTotalAmount());
    }
}