package kr.hhplus.be.server.order.domain.entity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class OrderAmountInfoTest {

    @Nested
    class 주문_가격_정보_생성 {

        @ParameterizedTest
        @ValueSource(ints = {-1000, -100, -10, -3, -2, -1})
        void 총_가격이_음수인_경우_IllegalArgumentException_발생(int totalAmount) {

            //when, then
            assertThatThrownBy(() -> new OrderAmountInfo(totalAmount, 50000, 20000))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("총 가격은 음수일 수 없습니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {-1000, -100, -10, -3, -2, -1})
        void 총_상품_가격이_음수인_경우_IllegalArgumentException_발생(int itemTotalAmount) {

            //when, then
            assertThatThrownBy(() -> new OrderAmountInfo(30000, itemTotalAmount, 20000))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("총 상품 가격은 음수일 수 없습니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {-1000, -100, -10, -3, -2, -1})
        void 할인_가격이_음수인_경우_IllegalArgumentException_발생(int discountAmount) {

            //when, then
            assertThatThrownBy(() -> new OrderAmountInfo(30000, 50000, discountAmount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("할인 가격은 음수일 수 없습니다.");
        }

        @Test
        void 총_상품_금액에서_할인금액을_뺀_값이_총_금액과_다른_경우_IllegalArgumentException_발생() {

            //when, then
            assertThatThrownBy(() -> new OrderAmountInfo(30000, 50000, 30000))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("가격 계산이 올바르지 않습니다.");
        }
    }

    @Nested
    class 할인금액_적용 {

        @Test
        void 할인금액_저장_및_총가격에_할인금액_뺀_값_적용() {

            //given
            OrderAmountInfo orderAmountInfo = OrderAmountInfo.of(50000, 50000, 0);

            //when
            OrderAmountInfo result = orderAmountInfo.applyDiscount(10000);

            //then
            assertThat(result).isEqualTo(OrderAmountInfo.of(40000, 50000, 10000));
        }
    }
}
