package kr.hhplus.be.server.product.infrastructure;

import kr.hhplus.be.server.product.domain.entity.PopularProduct;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.domain.entity.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class ProductRepositoryImplTest {
    @Autowired
    private ProductRepositoryImpl productRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private PopularProductJpaRepository popularProductJpaRepository;

    @BeforeEach
    void setUp() {
        popularProductJpaRepository.deleteAll();
        productJpaRepository.deleteAll();
    }

    @DisplayName("상품 ID 조회")
    @Test
    void findProductById() {
        //given
        Product product = productJpaRepository.save(Product.of(null, "상품명", Stock.of(100), 10000));

        //when
        Optional<Product> result = productRepository.findById(product.getId());

        //then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(product);
    }

    @DisplayName("여러 상품 ID로 상품 목록 조회")
    @Test
    void findProductsByIds() {
        //given
        List<Product> savedProduecs = productJpaRepository.saveAll(List.of(
            Product.of(null, "상품명", Stock.of(100), 10000),
            Product.of(null, "상품명", Stock.of(100), 10000)
            )
        );
        List<Long> ids = savedProduecs.stream().map(Product::getId).toList();

        //when
        List<Product> result = productRepository.findByIdIn(ids);

        //then
        assertThat(result).hasSize(2);
    }

    @DisplayName("인기 상품 저장")
    @Test
    void savePopularProduct() {
        //given
        List<PopularProduct> products = List.of(new PopularProduct(null, 1L, 1000, LocalDate.now(), 100, LocalDateTime.now()));

        //when
        List<PopularProduct> result = productRepository.savePopularProducts(products);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductId()).isEqualTo(1L);
    }

    @DisplayName("최근 3일간의 인기 상품 조회")
    @Test
    void findPopularProductsFromLastThreeDays() {
        //given
        PopularProduct today = new PopularProduct(null, 1L, 1000, LocalDate.now(), 100, LocalDateTime.now());
        PopularProduct oneDaysAgo = new PopularProduct(null, 1L, 1000, LocalDate.now().minusDays(1), 100, LocalDateTime.now());
        PopularProduct threeDaysAgo = new PopularProduct(null, 1L, 1000, LocalDate.now().minusDays(3), 100, LocalDateTime.now());
        PopularProduct fourDaysAgo = new PopularProduct(null, 1L, 1000, LocalDate.now().minusDays(4), 100, LocalDateTime.now());

        popularProductJpaRepository.saveAll(List.of(today, oneDaysAgo, threeDaysAgo, fourDaysAgo));

        //when
        List<PopularProduct> result = productRepository.findPopularProducts();

        //then
        assertThat(result).contains(oneDaysAgo);
        assertThat(result).contains(threeDaysAgo);
        assertThat(result).doesNotContain(today);
        assertThat(result).doesNotContain(fourDaysAgo);

    }
}
