package kr.hhplus.be.server.coupon.application.service;

import kr.hhplus.be.server.coupon.application.service.command.CouponCommand;
import kr.hhplus.be.server.coupon.domain.DiscountType;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.domain.entity.CouponIssue;
import kr.hhplus.be.server.coupon.infrastructure.CouponIssueJpaRepository;
import kr.hhplus.be.server.coupon.infrastructure.CouponJpaRepository;
import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.order.domain.entity.OrderAmountInfo;
import kr.hhplus.be.server.order.domain.entity.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class CouponServiceIntegrationTest {
    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private CouponIssueJpaRepository couponIssueJpaRepository;

    @BeforeEach
    void setUp() {
        couponIssueJpaRepository.deleteAll();
        couponJpaRepository.deleteAll();
    }

    @DisplayName("쿠폰 발급")
    @Test
    void issueCoupon() {
        // given
        Coupon coupon = couponJpaRepository.save(new Coupon(null, "쿠폰명", DiscountType.FIXED, 10000, LocalDateTime.now(), LocalDateTime.now().plusDays(30), 10, LocalDateTime.now(), LocalDateTime.now()));

        CouponCommand.CouponIssueCommand command = new CouponCommand.CouponIssueCommand(1L, coupon.getId());

        // when
        CouponIssue couponIssue = couponService.issueCoupon(command);

        // then
        assertThat(couponIssue).isNotNull();
        assertThat(couponIssue.getUserId()).isEqualTo(1L);
        assertThat(couponIssue.getCouponId()).isEqualTo(coupon.getId());
    }

    @DisplayName("정액 할인 쿠폰 적용")
    @Test
    void applyFixedDiscountCoupon() {
        // given
        Order order = new Order(null, 1L, 1L, OrderStatus.COMPLETE, OrderAmountInfo.of(10000, 10000, 0), LocalDateTime.now(), LocalDateTime.now());
        Coupon coupon = couponJpaRepository.save(new Coupon(null, "쿠폰명", DiscountType.FIXED, 1000, LocalDateTime.now(), LocalDateTime.now().plusMonths(3), 10, LocalDateTime.now(), LocalDateTime.now()));
        couponIssueJpaRepository.save(CouponIssue.of(1L, coupon));

        CouponCommand.CouponApplyCommand command = new CouponCommand.CouponApplyCommand(order, coupon.getId());

        // when
        int discountAmount = couponService.applyCoupon(command);

        // then
        assertThat(discountAmount).isEqualTo(1000);
    }

    @DisplayName("정률 할인 쿠폰 적용")
    @Test
    void applyRateDiscountCoupon() {
        // given
        Order order = new Order(null, 1L, 1L, OrderStatus.COMPLETE, OrderAmountInfo.of(10000, 10000, 0), LocalDateTime.now(), LocalDateTime.now());
        Coupon coupon = couponJpaRepository.save(new Coupon(null, "쿠폰명", DiscountType.RATE, 10, LocalDateTime.now(), LocalDateTime.now().plusMonths(3), 10, LocalDateTime.now(), LocalDateTime.now()));
        couponIssueJpaRepository.save(CouponIssue.of(1L, coupon));

        CouponCommand.CouponApplyCommand command = new CouponCommand.CouponApplyCommand(order, coupon.getId());

        // when
        int discountAmount = couponService.applyCoupon(command);

        // then
        assertThat(discountAmount).isEqualTo(1000);
    }
}
