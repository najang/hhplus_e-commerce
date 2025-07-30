package kr.hhplus.be.server.coupon.infrastructure;

import kr.hhplus.be.server.coupon.domain.DiscountType;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.domain.entity.CouponIssue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class CouponRepositoryImplTest {
    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private CouponIssueJpaRepository couponIssueJpaRepository;

    @Autowired
    private CouponRepositoryImpl couponRepository;

    @BeforeEach
    void setUp() {
        couponIssueJpaRepository.deleteAll();
        couponJpaRepository.deleteAll();
    }

    @DisplayName("쿠폰 저장 후 조회")
    @Test
    void saveCouponThenFindById() {
        //given
        Coupon coupon = new Coupon(1L, "쿠폰명", DiscountType.FIXED, 10000, LocalDateTime.now(), LocalDateTime.now().plusMonths(3), 10, LocalDateTime.now(), LocalDateTime.now());
        couponJpaRepository.save(coupon);

        //when
        Optional<Coupon> result = couponRepository.findCouponById(coupon.getId());

        //then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(coupon);
    }

    @DisplayName("쿠폰 발급 내역 저장 후 조회")
    @Test
    void saveCouponIssueHistoryThenFindByUserIdAndCouponId() {
        //given
        CouponIssue couponIssue = new CouponIssue(null, 1L, "쿠폰명1", DiscountType.FIXED, 10000, 1L, LocalDateTime.now().plusMonths(3), false, LocalDateTime.now());
        couponIssueJpaRepository.save(couponIssue);

        //when
        Optional<CouponIssue> result = couponRepository.findByUserIdAndCouponId(couponIssue.getUserId(), couponIssue.getCouponId());

        //then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(couponIssue);
    }

    @DisplayName("쿠폰 발급 내역 저장")
    @Test
    void saveCouponIssueHistory() {
        //given
        CouponIssue couponIssue = new CouponIssue(null, 1L, "쿠폰명1", DiscountType.FIXED, 10000, 1L, LocalDateTime.now().plusMonths(3), false, LocalDateTime.now());

        //when
        CouponIssue result = couponRepository.saveCouponIssue(couponIssue);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result).isEqualTo(couponIssue);
    }

    @DisplayName("유저의 쿠폰 발급 내역 존재 여부 확인")
    @Test
    void existsCouponIssueHistoryByUserIdAndCouponId() {
        //given
        CouponIssue couponIssue = new CouponIssue(null, 1L, "쿠폰명1", DiscountType.FIXED, 10000, 1L, LocalDateTime.now().plusMonths(3), false, LocalDateTime.now());

        //when
        boolean result = couponRepository.existsCouponIssueByUserIdAndCouponId(couponIssue.getUserId(), couponIssue.getCouponId());

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("유저의 쿠폰 발급 내역 목록 조회")
    @Test
    void findAllCouponIssueHistoriesByUserId() {
        //given
        List<CouponIssue> issues = List.of(
                new CouponIssue(null, 1L, "쿠폰명1", DiscountType.FIXED, 10000, 1L, LocalDateTime.now().plusMonths(3), false, LocalDateTime.now()),
                new CouponIssue(null, 1L, "쿠폰명2", DiscountType.RATE, 10000, 1L, LocalDateTime.now().plusMonths(3), false, LocalDateTime.now())
        );
        couponIssueJpaRepository.saveAll(issues);

        //when
        List<CouponIssue> result = couponRepository.findByUserId(1L);

        //then
        assertThat(result).hasSize(2);
    }
}
