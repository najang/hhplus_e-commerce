package kr.hhplus.be.server.order.domain.entity;

import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.domain.entity.Stock;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OrderTest {

    @Nested
    class 주문_생성 {

        @ParameterizedTest
        @ValueSource(longs = {-1000L, -100L, -10L, -3L, -2L, -1L})
        void 유저식별자가_음수인_경우_IllegalArgumentException_발생(long userId) {

            //when, then
            assertThatThrownBy(() -> new Order(1L, userId, 1L, OrderStatus.COMPLETE, OrderAmountInfo.of(30000, 50000, 20000), LocalDateTime.now(), LocalDateTime.now()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("유저식별자는 음수일 수 없습니다.");
        }

        @ParameterizedTest
        @NullSource
        void 주문_상태가_null_인_경우_IllegalArgumentException_발생(OrderStatus orderStatus) {

            //when, then
            assertThatThrownBy(() -> new Order(1L, 1L, 1L, orderStatus, OrderAmountInfo.of(30000, 50000, 20000), LocalDateTime.now(), LocalDateTime.now()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 상태 정보가 필요합니다.");
        }

        @ParameterizedTest
        @NullSource
        void 주문_가격_정보가_null_인_경우_IllegalArgumentException_발생(OrderAmountInfo orderAmountInfo) {

            //when, then
            assertThatThrownBy(() -> new Order(1L, 1L, 1L, OrderStatus.COMPLETE, orderAmountInfo, LocalDateTime.now(), LocalDateTime.now()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 가격 정보가 필요합니다.");
        }
    }

    @Nested
    class 주문_가격_계산 {

        @ParameterizedTest
        @NullSource
        void 주문_상품_정보가_null_인_경우_IllegalArgumentException_발생(List<OrderProduct> orderProducts) {

            //given
            Order order = new Order(1L, 1L, 1L, OrderStatus.COMPLETE, OrderAmountInfo.of(30000, 50000, 20000), LocalDateTime.now(), LocalDateTime.now());

            //when, then
            assertThatThrownBy(() -> order.calculateOrderAmount(orderProducts))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 상품 정보가 필요합니다.");
        }

        @Test
        void 주문_상품의_가격과_주문_수량_곱을_모두_더하여_총_금액을_구한다() {
            //given
            Order order = new Order(1L, 1L, 1L, OrderStatus.COMPLETE, OrderAmountInfo.of(30000, 50000, 20000), LocalDateTime.now(), LocalDateTime.now());

            List<Product> products = List.of(
                    Product.of(1L, "상품명1", Stock.of(10), 10000),
                    Product.of(2L, "상품명2", Stock.of(10), 20000),
                    Product.of(3L, "상품명3", Stock.of(10), 30000)
            );

            List<OrderProduct> orderProducts = List.of(
                    OrderProduct.of(order, products.get(0), 2),
                    OrderProduct.of(order, products.get(1), 1),
                    OrderProduct.of(order, products.get(2), 1)
            );

            //when
            order.calculateOrderAmount(orderProducts);

            //then
            assertThat(order.getOrderAmountInfo()).isEqualTo(OrderAmountInfo.of(70000, 70000, 0));
        }
    }

}
