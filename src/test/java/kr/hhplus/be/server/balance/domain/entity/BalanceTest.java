package kr.hhplus.be.server.balance.domain.entity;

import kr.hhplus.be.server.balance.application.exception.BalanceErrorCode;
import kr.hhplus.be.server.common.exception.CustomException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class BalanceTest {

    @Nested
    class 잔액_생성 {

        @ParameterizedTest
        @ValueSource(longs = {-1000L, -100L, -10L, -3L, -2L, -1L})
        void 유저식별자가_음수면_CustomException_발생(long userId) {

            //when, then
            assertThatThrownBy(() -> Balance.of(1L, userId, Amount.of(1000)))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(BalanceErrorCode.INVALID_NEGATIVE_USER_ID.getMessage());
        }

        @ParameterizedTest
        @NullSource
        void 금액이_null_이면_CustomException_발생(Amount amount) {

            //when, then
            assertThatThrownBy(() -> Balance.of(1L, 1L, amount))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(BalanceErrorCode.BALANCE_INFORMATION_REQUIRED.getMessage());
        }
    }

    @Nested
    class 잔액_충전 {

        @ParameterizedTest
        @ValueSource(ints = {500001, 500002, 500003, 1000000})
        void 잔액에_충전금액을_더한_값이_최대_한도_초과면_CustomException_발생(int value) {

            //given
            Balance balance = Balance.of(1L, 1L, Amount.of(500000));

            //when, then
            assertThatThrownBy(() -> balance.charge(value))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(BalanceErrorCode.RECHARGE_LIMIT_EXCEEDED.getMessage());
        }
    }

    @Nested
    class 잔액_사용 {

        @ParameterizedTest
        @ValueSource(ints = {500001, 500002, 500003, 1000000})
        void 잔액에_사용금액을_뺀_값이_음수면_CustomException_발생(int value) {

            //given
            Balance balance = Balance.of(1L, 1L, Amount.of(500000));

            //when, then
            assertThatThrownBy(() -> balance.use(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(BalanceErrorCode.INSUFFICIENT_BALANCE.getMessage());
        }
    }
}
