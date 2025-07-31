package kr.hhplus.be.server.product.application.service;

import kr.hhplus.be.server.order.domain.entity.OrderProduct;
import kr.hhplus.be.server.product.application.service.command.ProductCommand;
import kr.hhplus.be.server.product.domain.entity.PopularProduct;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.domain.entity.Stock;
import kr.hhplus.be.server.product.infrastructure.PopularProductJpaRepository;
import kr.hhplus.be.server.product.infrastructure.ProductJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class ProductServiceIntegrationTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private PopularProductJpaRepository popularProductJpaRepository;

    @BeforeEach
    void setUp() {
        popularProductJpaRepository.deleteAll();
        productJpaRepository.deleteAll();
    }

    @DisplayName("재고 차감")
    @Test
    void decreaseStock() {
        // given
        Product product1 = new Product(null, "상품명1", Stock.of(10), 100, LocalDateTime.now(), LocalDateTime.now());
        Product product2 = new Product(null, "상품명2", Stock.of(10), 100, LocalDateTime.now(), LocalDateTime.now());

        productJpaRepository.saveAll(List.of(product1, product2));

        List<ProductCommand.StockDecreaseCommand> commands = List.of(
            ProductCommand.StockDecreaseCommand.of(product1.getId(), 1),
            ProductCommand.StockDecreaseCommand.of(product2.getId(), 2)
        );

        // when
        List<Product> result = productService.decreaseStocks(commands);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStock()).isEqualTo(9); // 재고 감소 확인
        assertThat(result.get(1).getStock()).isEqualTo(8); // 재고 감소 확인
    }

    @Test
    void 인기_상품_통계_생성_시_상품식별자별로_그룹핑하여_생성() {
        // given
        List<OrderProduct> orderProducts = List.of(
                new OrderProduct(null, 1L, 1L, "상품명1", 10000, 1),
                new OrderProduct(null, 1L, 1L, "상품명1", 10000, 1),
                new OrderProduct(null, 1L, 2L, "상품명2", 10000, 1)
        );

        // when
        List<PopularProduct> result = productService.createPopularProductStatistics(orderProducts);

        // then
        assertThat(result).hasSize(2);
    }
}
