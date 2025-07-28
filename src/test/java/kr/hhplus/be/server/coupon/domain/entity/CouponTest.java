package kr.hhplus.be.server.coupon.domain.entity;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.coupon.domain.DiscountType;
import kr.hhplus.be.server.coupon.application.exception.CouponErrorCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class CouponTest {

    @Nested
    class 쿠폰_생성 {

        @ParameterizedTest
        @NullAndEmptySource
        void 쿠폰명이_비어있으면_CustomException_발생(String couponName) {

            //when, then
            assertThatThrownBy(() -> new Coupon(1L, couponName, DiscountType.FIXED, 10000, LocalDateTime.MIN, LocalDateTime.MAX, 100, LocalDateTime.now(), LocalDateTime.now()))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(CouponErrorCode.COUPON_NAME_REQUIRED.getMessage());
        }

        @ParameterizedTest
        @NullSource
        void 할인타입이_null_이면_CustomException_발생(DiscountType discountType) {

            //when, then
            assertThatThrownBy(() -> new Coupon(1L, "쿠폰명", discountType, 10000, LocalDateTime.MIN, LocalDateTime.MAX, 100, LocalDateTime.now(), LocalDateTime.now()))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(CouponErrorCode.DISCOUNT_TYPE_INFORMATION_REQUIRED.getMessage());
        }

        @ParameterizedTest
        @NullSource
        void 유효한_시작일시_null_이면_CustomException_발생(LocalDateTime validTo) {

            //when, then
            assertThatThrownBy(() -> new Coupon(1L, "쿠폰명", DiscountType.FIXED, 10000, validTo, LocalDateTime.MAX, 100, LocalDateTime.now(), LocalDateTime.now()))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(CouponErrorCode.COUPON_VALID_PERIOD_REQUIRED.getMessage());
        }


        @ParameterizedTest
        @NullSource
        void 유효한_종료일시_null_이면_CustomException_발생(LocalDateTime validFrom) {

            //when, then
            assertThatThrownBy(() -> new Coupon(1L, "쿠폰명", DiscountType.FIXED, 10000, LocalDateTime.MIN, validFrom, 100, LocalDateTime.now(), LocalDateTime.now()))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(CouponErrorCode.COUPON_VALID_PERIOD_REQUIRED.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {-10000, -10, -3, -2, -1, 0})
        void 할인율_할인금액이_0이하이면_CustomException_발생(int discountValue) {

            //when, then
            assertThatThrownBy(() -> new Coupon(1L, "쿠폰명", DiscountType.FIXED, discountValue, LocalDateTime.MIN, LocalDateTime.MAX, 100, LocalDateTime.now(), LocalDateTime.now()))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(CouponErrorCode.DISCOUNT_VALUE_MUST_BE_POSITIVE.getMessage());
        }


        @ParameterizedTest
        @ValueSource(ints = {-10000, -10, -3, -2, -1, 0})
        void 수량이_0이하이면_CustomException_발생(int count) {

            //when, then
            assertThatThrownBy(() -> new Coupon(1L, "쿠폰명", DiscountType.FIXED, 10000, LocalDateTime.MIN, LocalDateTime.MAX, count, LocalDateTime.now(), LocalDateTime.now()))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(CouponErrorCode.COUPON_QUANTITY_MUST_BE_POSITIVE.getMessage());
        }
    }

    @Nested
    class 쿠폰_발급 {
        @Test
        void 유효한_시작일시가_현재_이후이면_CustomException_발생() {
            //given
            Coupon coupon = new Coupon(1L, "쿠폰명", DiscountType.FIXED, 10000, LocalDateTime.MAX, LocalDateTime.MAX, 100, LocalDateTime.now(), LocalDateTime.now());

            //when, then
            assertThatThrownBy(coupon::issue)
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(CouponErrorCode.INVALID_COUPON.getMessage());
        }

        @Test
        void 유효한_종료일시가_현재_이전이면_CustomException_발생() {
            //given
            Coupon coupon = new Coupon(1L, "쿠폰명", DiscountType.FIXED, 10000, LocalDateTime.MIN, LocalDateTime.MIN, 100, LocalDateTime.now(), LocalDateTime.now());

            //when, then
            assertThatThrownBy(coupon::issue)
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(CouponErrorCode.INVALID_COUPON.getMessage());
        }

        @Test
        void 수량이_없는_경우_CustomException_발생() {
            //given
            Coupon coupon = new Coupon(1L, "쿠폰명", DiscountType.FIXED, 10000, LocalDateTime.MIN, LocalDateTime.MAX, 0, LocalDateTime.now(), LocalDateTime.now());

            //when, then
            assertThatThrownBy(coupon::issue)
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(CouponErrorCode.COUPON_ISSUANCE_LIMIT_EXHAUSTED.getMessage());
        }

        @Test
        void 정상_발급시_수량_하나_감소() {
            //given
            Coupon coupon = new Coupon(1L, "쿠폰명", DiscountType.FIXED, 10000, LocalDateTime.MIN, LocalDateTime.MAX, 10, LocalDateTime.now(), LocalDateTime.now());

            //when
            coupon.issue();

            //then
            assertThat(coupon.getCount()).isEqualTo(9);
        }
    }
}