package kr.hhplus.be.server.coupon.application.service;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.coupon.application.command.CouponCommand;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.domain.entity.CouponIssue;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.application.exception.CouponErrorCode;
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
                .orElseThrow(() -> new CustomException(CouponErrorCode.COUPON_NOT_OWNED_BY_USER));

        return couponIssue.applyDiscount(command.getTotalAmount());
    }

    @Transactional
    public CouponIssue issueCoupon(CouponCommand.CouponIssueCommand command) {
        if(couponRepository.existsCouponIssueByUserIdAndCouponId(command.userId(), command.couponId())) {
            throw new CustomException(CouponErrorCode.COUPON_ALREADY_ISSUED);
        }

        Coupon coupon = couponRepository.findCouponById(command.couponId())
                .orElseThrow(() -> {
                    throw new CustomException(CouponErrorCode.COUPON_NOT_FOUND);
                });

        coupon.issue();

        CouponIssue couponIssue = CouponIssue.of(command.userId(), coupon);

        return couponRepository.saveCouponIssue(couponIssue);
    }
}