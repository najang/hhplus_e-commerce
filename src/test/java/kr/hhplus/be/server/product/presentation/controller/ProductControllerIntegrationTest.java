package kr.hhplus.be.server.product.presentation.controller;

import kr.hhplus.be.server.product.application.dto.request.ProductRequest;
import kr.hhplus.be.server.product.application.dto.response.ProductResponse;
import kr.hhplus.be.server.product.domain.entity.PopularProduct;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.domain.entity.Stock;
import kr.hhplus.be.server.product.infrastructure.PopularProductJpaRepository;
import kr.hhplus.be.server.product.infrastructure.ProductJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ProductControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private PopularProductJpaRepository popularProductJpaRepository;

    @BeforeEach
    void setUp() {
        popularProductJpaRepository.deleteAll();
        productJpaRepository.deleteAll();
    }

    @DisplayName("상품 조회")
    @Test
    void getProductById() {

        // given
        Product product = productJpaRepository.save(Product.of(null, "상품명", Stock.of(100), 10000));

        // when
        ResponseEntity<ProductResponse.ProductDetailResponse> response = restTemplate.getForEntity(
                "/api/v1/product/" + product.getId(),
                ProductResponse.ProductDetailResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(product.getId());
    }

    @DisplayName("양수가 아닌 상품식별자로 상품 조회")
    @ParameterizedTest
    @ValueSource(longs = {-100L, -10L, -3L, -2L, -1L, 0L})
    void getProductOrThrowIfInvalidId(long itemId) {

        // when
        ResponseEntity<ProductResponse.ProductDetailResponse> response = restTemplate.getForEntity(
                "/api/v1/product/" + itemId,
                ProductResponse.ProductDetailResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("인기 상품 목록 조회")
    @Test
    void getPopularProducts() {
        // given
        popularProductJpaRepository.saveAll(List.of(
                new PopularProduct(null, 1L, 1000, LocalDate.now().minusDays(1), 100, LocalDateTime.now()),
                new PopularProduct(null, 1L, 1000, LocalDate.now().minusDays(3), 100, LocalDateTime.now())
        ));

        // when
        ResponseEntity<ProductResponse.PopularProductDetailResponse[]> response = restTemplate.getForEntity(
                "/api/v1/product/popular",
                ProductResponse.PopularProductDetailResponse[].class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()).hasSize(2);
    }
}
