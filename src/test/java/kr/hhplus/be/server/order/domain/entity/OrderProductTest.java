package kr.hhplus.be.server.order.domain.entity;

import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.domain.entity.Stock;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class OrderProductTest {

    @Nested
    class 주문_상품_생성 {

        @ParameterizedTest
        @ValueSource(longs = {-1000L, -100L, -10L, -3L, -2L, -1L})
        void 주문식별자가_음수인_경우_IllegalArgumentException_발생(long orderId) {

            //when, then
            assertThatThrownBy(() -> new OrderProduct(1L, orderId, 1L, "상품명", 10000, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문식별자는 음수일 수 없습니다.");
        }

        @ParameterizedTest
        @ValueSource(longs = {-1000L, -100L, -10L, -3L, -2L, -1L})
        void 상품식별자가_음수인_경우_IllegalArgumentException_발생(long itemId) {

            //when, then
            assertThatThrownBy(() -> new OrderProduct(1L, 1L, itemId, "상품명", 10000, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("상품식별자는 음수일 수 없습니다.");
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 상품명이_비어있는_경우_IllegalArgumentException_발생(String itemName) {

            //when, then
            assertThatThrownBy(() -> new OrderProduct(1L, 1L, 1L, itemName, 10000, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("상품명을 입력해주세요.");
        }

        @ParameterizedTest
        @ValueSource(ints = {-1000, -100, -10, -3, -2, -1})
        void 상품_가격이_음수인_경우_IllegalArgumentException_발생(int sellPrice) {

            //when, then
            assertThatThrownBy(() -> new OrderProduct(1L, 1L, 1L, "상품명", sellPrice, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("상품 가격은 음수일 수 없습니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {-1000, -100, -10, -3, -2, -1, 0})
        void 주문수량이_양수가_아닌_경우_IllegalArgumentException_발생(int count) {

            //when, then
            assertThatThrownBy(() -> new OrderProduct(1L, 1L, 1L, "상품명", 10000, count))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 수량은 양수여야 합니다.");
        }

        @ParameterizedTest
        @NullSource
        void 주문_정보가_null_인_경우_IllegalArgumentException_발생(Order order) {

            //given
            Product product = Product.of(1L, "상품명", Stock.of(1), 10000);

            //when, then
            assertThatThrownBy(() -> OrderProduct.of(order, product, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 정보가 필요합니다.");
        }

        @ParameterizedTest
        @NullSource
        void 상품_정보가_null_인_경우_IllegalArgumentException_발생(Product product) {

            //given
            Order order = Order.of(1L);

            //when, then
            assertThatThrownBy(() -> OrderProduct.of(order, product, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("상품 정보가 필요합니다.");
        }
    }

    @Nested
    class 주문_가격_계산 {

        @Test
        void 주문_상품_가격과_주문_수량을_곱한_값을_전달() {

            //given
            OrderProduct orderProduct = new OrderProduct(1L, 1L, 1L, "상품명", 10000, 5);

            //when
            int result = orderProduct.getOrderProductPrice();

            //then
            assertThat(result).isEqualTo(50000);
        }
    }
}