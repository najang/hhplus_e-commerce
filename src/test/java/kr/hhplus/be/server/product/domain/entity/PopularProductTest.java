package kr.hhplus.be.server.product.domain.entity;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.product.exception.ProductErrorCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class PopularProductTest {

    @Nested
    class 인기_상품_생성 {

        @ParameterizedTest
        @ValueSource(longs = {-1000L, -100L, -10L, -3L, -2L, -1L})
        void 상품식별자가_음수일_경우_CustomException_발생(long productId) {

            //when, then
            assertThatThrownBy(() -> PopularProduct.of(productId, 100, LocalDate.now(), 100, LocalDateTime.now()))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(ProductErrorCode.INVALID_NEGATIVE_PRODUCT_ID.getMessage());
        }

        @ParameterizedTest
        @NullSource
        void 주문날짜가_null_인_경우_CustomException_발생(LocalDate orderDate) {

            //when, then
            assertThatThrownBy(() -> PopularProduct.of(1L, 100, orderDate, 100, LocalDateTime.now()))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(ProductErrorCode.ORDER_DATE_INFORMATION_REQUIRED.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {-1000, -100, -10, -3, -2, -1})
        void 주문수량이_음수일_경우_CustomException_발생(int orderCount) {

            //when, then
            assertThatThrownBy(() -> PopularProduct.of(1L, 100, LocalDate.now(), orderCount, LocalDateTime.now()))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(ProductErrorCode.INVALID_NEGATIVE_ORDER_QUANTITY.getMessage());
        }
    }
}