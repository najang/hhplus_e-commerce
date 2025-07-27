package kr.hhplus.be.server.balance.domain.entity;

import kr.hhplus.be.server.balance.application.exception.BalanceErrorCode;
import kr.hhplus.be.server.common.exception.CustomException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AmountTest {

    @Nested
    class 금액_생성 {

        @ParameterizedTest
        @ValueSource(ints = {-1000, -100, -10, -3, -2, -1})
        void 금액이_음수면_CustomException_발생(int value) {

            //when, then
            assertThatThrownBy(() -> Amount.of(value))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(BalanceErrorCode.INVALID_NEGATIVE_AMOUNT.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1000, 100, 10, 1, 0, -999, -1000})
    void 더한_금액이_0이상이면_정상_증가(int value) {

        //given
        Amount amount = Amount.of(1000);

        //when
        Amount result = amount.add(value);

        //then
        assertThat(result).isEqualTo(Amount.of(1000 + value));
    }

    @ParameterizedTest
    @ValueSource(ints = {1000, 999, 100, 10, 1, 0, -999, -1000})
    void 뺀_금액이_0이상이면_정상_차감(int value) {

        //given
        Amount amount = Amount.of(1000);

        //when
        Amount result = amount.sub(value);

        //then
        assertThat(result).isEqualTo(Amount.of(1000 - value));
    }
}