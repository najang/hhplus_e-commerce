package kr.hhplus.be.server.product.application.service;


import kr.hhplus.be.server.product.application.command.ProductCommand;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.domain.entity.Stock;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Nested
    class 상품_상세_조회 {

        @Test
        void 상품이_존재하지_않는_경우_RuntimeException_발생() {

            //given
            when(productRepository.findById(1L))
                    .thenReturn(Optional.empty());

            //when, then
            assertThatThrownBy(() -> productService.findById(1L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("상품을 찾을 수 없습니다.");

            verify(productRepository, times(1)).findById(1L);
        }

        @Test
        void 상품식별자로_상품_조회_레포지토리_1회_호출() {

            //given
            when(productRepository.findById(1L))
                    .thenReturn(Optional.of(Product.of(1L, "상품명", Stock.of(100), 10000)));

            //when, then
            productService.findById(1L);

            verify(productRepository, times(1)).findById(1L);
        }

        @Test
        void 인기_상품_목록_조회_레포지토리_1회_호출() {

            //given
            when(productRepository.findPopularProducts())
                    .thenReturn(List.of());

            //when
            productService.findPopularProducts();

            //then
            verify(productRepository, times(1)).findPopularProducts();
        }
    }

    @Nested
    class 재고_차감 {

        @Test
        void 상품_목록_조회_레포지토리_1회_호출() {

            //given
            List<ProductCommand.StockDecreaseCommand> commands = List.of(
                    ProductCommand.StockDecreaseCommand.of(1L, 1),
                    ProductCommand.StockDecreaseCommand.of(2L, 2)
            );

            when(productRepository.findByIdIn(List.of(1L, 2L)))
                    .thenReturn(List.of(
                            Product.of(1L, "상품명1", Stock.of(10), 10000),
                            Product.of(2L, "상품명2", Stock.of(10), 20000)
                    ));

            //when
            productService.decreaseStocks(commands);

            //then
            verify(productRepository, times(1)).findByIdIn(List.of(1L, 2L));
        }
    }
}