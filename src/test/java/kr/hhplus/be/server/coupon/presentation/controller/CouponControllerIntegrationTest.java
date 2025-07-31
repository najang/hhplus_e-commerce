package kr.hhplus.be.server.coupon.presentation.controller;

import kr.hhplus.be.server.coupon.application.dto.request.CouponRequest;
import kr.hhplus.be.server.coupon.application.dto.response.CouponResponse;
import kr.hhplus.be.server.coupon.domain.DiscountType;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.domain.entity.CouponIssue;
import kr.hhplus.be.server.coupon.infrastructure.CouponIssueJpaRepository;
import kr.hhplus.be.server.coupon.infrastructure.CouponJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class CouponControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private CouponIssueJpaRepository couponIssueJpaRepository;

    @BeforeEach
    void setUp() {
        couponIssueJpaRepository.deleteAll();
        couponJpaRepository.deleteAll();
    }

    @DisplayName("유저의 보유 쿠폰 목록 조회")
    @Test
    void getUserCoupons() {
        // given
        Long userId = 1L;
        Coupon coupon1 = couponJpaRepository.save(new Coupon(null, "쿠폰명", DiscountType.FIXED, 10000, LocalDateTime.now(), LocalDateTime.now().plusMonths(3), 10, LocalDateTime.now(), LocalDateTime.now()));
        Coupon coupon2 = couponJpaRepository.save(new Coupon(null, "쿠폰명", DiscountType.FIXED, 10000, LocalDateTime.now(), LocalDateTime.now().plusMonths(3), 10, LocalDateTime.now(), LocalDateTime.now()));
        couponIssueJpaRepository.saveAll(List.of(
                CouponIssue.of(userId, coupon1),
                CouponIssue.of(userId, coupon2)
        ));

        // when
        ResponseEntity<CouponResponse.UserCouponResponse[]> response = restTemplate.getForEntity(
                "/api/v1/coupon/user/" + userId,
                CouponResponse.UserCouponResponse[].class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()[0].couponId()).isEqualTo(coupon1.getId());
        assertThat(response.getBody()[1].couponId()).isEqualTo(coupon2.getId());
    }

    @DisplayName("양수가 아닌 유저식별자로 보유 쿠폰 목록 조회")
    @ParameterizedTest
    @ValueSource(longs = {-100L, -10L, -3L, -2L, -1L, 0L})
    void getUserCouponsWithInvalidUserId(long userId) {
        // when
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/coupon/user/" + userId,
                String.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("쿠폰 발급")
    @Test
    void issueCoupon() {
        // given
        Coupon coupon = couponJpaRepository.save(new Coupon(null, "쿠폰명", DiscountType.FIXED, 10000, LocalDateTime.now(), LocalDateTime.now().plusMonths(3), 10, LocalDateTime.now(), LocalDateTime.now()));

        CouponRequest.CouponIssueRequest request = new CouponRequest.CouponIssueRequest(1L, coupon.getId());

        // when
        ResponseEntity<CouponResponse.UserCouponResponse> response = restTemplate.postForEntity(
                "/api/v1/coupon/issue",
                request,
                CouponResponse.UserCouponResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().couponId()).isEqualTo(coupon.getId());
    }

    @DisplayName("양수가 아닌 유저식별자로 쿠폰 발급")
    @ParameterizedTest
    @ValueSource(longs = {-100L, -10L, -3L, -2L, -1L, 0L})
    void issueCouponWithInvalidUserId(long userId) {
        // given
        CouponRequest.CouponIssueRequest request = new CouponRequest.CouponIssueRequest(userId, 1L);

        // when
        ResponseEntity<CouponResponse.UserCouponResponse> response = restTemplate.postForEntity(
                "/api/v1/coupon/issue",
                request,
                CouponResponse.UserCouponResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("양수가 아닌 쿠폰식별자로 쿠폰 발급")
    @ParameterizedTest
    @ValueSource(longs = {-100L, -10L, -3L, -2L, -1L, 0L})
    void issueCouponWithInvalidCouponId(long couponId) {
        // given
        CouponRequest.CouponIssueRequest request = new CouponRequest.CouponIssueRequest(1L, couponId);

        // when
        ResponseEntity<CouponResponse.UserCouponResponse> response = restTemplate.postForEntity(
                "/api/v1/coupon/issue",
                request,
                CouponResponse.UserCouponResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
