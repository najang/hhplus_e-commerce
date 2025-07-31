package kr.hhplus.be.server.order.presentation.controlller;

import kr.hhplus.be.server.balance.domain.TransactionType;
import kr.hhplus.be.server.balance.domain.entity.Amount;
import kr.hhplus.be.server.balance.domain.entity.Balance;
import kr.hhplus.be.server.balance.infrastructure.BalanceJpaRepository;
import kr.hhplus.be.server.balance.infrastructure.PointHistoryJpaRepository;
import kr.hhplus.be.server.coupon.domain.DiscountType;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.domain.entity.CouponIssue;
import kr.hhplus.be.server.coupon.infrastructure.CouponIssueJpaRepository;
import kr.hhplus.be.server.coupon.infrastructure.CouponJpaRepository;
import kr.hhplus.be.server.order.application.dto.request.OrderRequest;
import kr.hhplus.be.server.order.application.dto.response.OrderResponse;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.domain.entity.Stock;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class OrderControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private BalanceJpaRepository balanceJpaRepository;

    @Autowired
    private PointHistoryJpaRepository pointHistoryJpaRepository;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private CouponIssueJpaRepository couponIssueJpaRepository;

    @BeforeEach
    void setUp() {
        productJpaRepository.deleteAll();
        balanceJpaRepository.deleteAll();
        pointHistoryJpaRepository.deleteAll();
        couponJpaRepository.deleteAll();
        couponIssueJpaRepository.deleteAll();
    }

    @DisplayName("주문 생성")
    @Test
    void createOrder() {

        // given
        Product product = productJpaRepository.save(Product.of(null, "상품명", Stock.of(100), 10000));

        Balance balance = balanceJpaRepository.save(new Balance(null, 1L, Amount.of(20000), LocalDateTime.now()));

        Coupon coupon = couponJpaRepository.save(new Coupon(null, "쿠폰명", DiscountType.FIXED, 1000, LocalDateTime.now(), LocalDateTime.now().plusMonths(3), 10, LocalDateTime.now(), LocalDateTime.now()));
        couponIssueJpaRepository.save(CouponIssue.of(1L, coupon));

        OrderRequest.OrderCreateRequest request = new OrderRequest.OrderCreateRequest(
                1L,
                coupon.getId(),
                List.of(new OrderRequest.OrderProductCreateRequest(product.getId(), 2))
        );

        // when
        ResponseEntity<OrderResponse.OrderDetailResponse> response = restTemplate.postForEntity(
                "/api/v1/order",
                request,
                OrderResponse.OrderDetailResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().totalAmount()).isEqualTo(19000);
        assertThat(response.getBody().discountAmount()).isEqualTo(1000);
        assertThat(response.getBody().productTotalAmount()).isEqualTo(20000);

        assertThat(balanceJpaRepository.findByUserId(balance.getUserId()).get().getAmount()).isEqualTo(1000);

        assertThat(pointHistoryJpaRepository.findAll()).hasSize(1);
        assertThat(pointHistoryJpaRepository.findAll().get(0).getType()).isEqualTo(TransactionType.USE);
        assertThat(pointHistoryJpaRepository.findAll().get(0).getOrderId()).isEqualTo(response.getBody().orderId());
        assertThat(pointHistoryJpaRepository.findAll().get(0).getAmount()).isEqualTo(Amount.of(19000));
    }

    @DisplayName("양수가 아닌 유저식별자로 주문")
    @ParameterizedTest
    @ValueSource(longs = {-100L, -10L, -3L, -2L, -1L, 0L})
    void orderWithInvalidUserId(long userId) {
        // given
        OrderRequest.OrderCreateRequest request = new OrderRequest.OrderCreateRequest(
                userId,
                1L,
                List.of(new OrderRequest.OrderProductCreateRequest(1L, 2))
        );

        // when
        ResponseEntity<OrderResponse.OrderDetailResponse> response = restTemplate.postForEntity(
                "/api/v1/order",
                request,
                OrderResponse.OrderDetailResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("양수가 아닌 상품식별자로 주문")
    @ParameterizedTest
    @ValueSource(longs = {-100L, -10L, -3L, -2L, -1L, 0L})
    void orderWithInvalidProductId(long productId) {
        // given
        OrderRequest.OrderCreateRequest request = new OrderRequest.OrderCreateRequest(
                1L,
                1L,
                List.of(new OrderRequest.OrderProductCreateRequest(productId, 2))
        );

        // when
        ResponseEntity<OrderResponse.OrderDetailResponse> response = restTemplate.postForEntity(
                "/api/v1/order",
                request,
                OrderResponse.OrderDetailResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("양수가 아닌 주문수량으로 주문")
    @ParameterizedTest
    @ValueSource(ints = {-100, -10, -3, -2, -1, 0})
    void orderWithInvalidOrderCount(int count) {

        // given
        OrderRequest.OrderCreateRequest request = new OrderRequest.OrderCreateRequest(
                1L,
                1L,
                List.of(new OrderRequest.OrderProductCreateRequest(1L, count))
        );

        // when
        ResponseEntity<OrderResponse.OrderDetailResponse> response = restTemplate.postForEntity(
                "/api/v1/order",
                request,
                OrderResponse.OrderDetailResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
