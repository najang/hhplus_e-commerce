package kr.hhplus.be.server.order.infrastructure;

import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.order.domain.entity.OrderAmountInfo;
import kr.hhplus.be.server.order.domain.entity.OrderProduct;
import kr.hhplus.be.server.order.domain.entity.OrderStatus;
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
public class OrderRepositoryImplTest {
    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private OrderProductJpaRepository orderProductJpaRepository;

    @Autowired
    private OrderRepositoryImpl orderRepository;

    @BeforeEach
    void setUp() {
        orderProductJpaRepository.deleteAll();
        orderJpaRepository.deleteAll();
    }

    @DisplayName("주문 저장")
    @Test
    void saveOrder() {
        //given
        Order order = new Order(null, 1L, 1L, OrderStatus.COMPLETE, OrderAmountInfo.of(30000, 50000, 20000), LocalDateTime.now(), LocalDateTime.now());

        //then
        Order result = orderRepository.saveOrder(order);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result).isEqualTo(order);
    }

    @DisplayName("주문 상품 저장")
    @Test
    void saveOrderProducts() {
        //given
        OrderProduct product1 = new OrderProduct(null, 1L, 1L, "상품명1", 10000, 5);
        OrderProduct product2 = new OrderProduct(null, 1L, 2L, "상품명2", 20000, 5);

        //when
        List<OrderProduct> result = orderRepository.saveOrderProducts(List.of(product1, product2));

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(product1);
        assertThat(result.get(1)).isEqualTo(product2);
    }

    @DisplayName("오늘 주문한 상품들 조회")
    @Test
    void findOrderProductsByTodayOrders() {
        // given
        LocalDateTime today = LocalDateTime.now();
        Order todayOrder = new Order(null, 1L, 1L, OrderStatus.COMPLETE, OrderAmountInfo.of(30000, 50000, 20000), today, today);
        orderJpaRepository.save(todayOrder);
        orderProductJpaRepository.saveAll(List.of(
            new OrderProduct(null, todayOrder.getId(), 1L, "상품명1", 10000, 1),
            new OrderProduct(null, todayOrder.getId(), 2L, "상품명2", 20000, 1)
        ));

        LocalDateTime yesterday = today.minusDays(1);
        Order yesterdayOrder = new Order(null, 1L, 1L, OrderStatus.COMPLETE, OrderAmountInfo.of(30000, 50000, 20000), yesterday, yesterday);
        orderJpaRepository.save(yesterdayOrder);
        orderProductJpaRepository.save(
                new OrderProduct(null, yesterdayOrder.getId(), 1L, "상품명1", 10000, 1)
        );

        // when
        List<OrderProduct> result = orderRepository.findTodayOrderProducts();

        // then
        assertThat(result).hasSize(2); // 오늘 주문 2건만 나와야 함
        assertThat(result).allMatch(orderProduct ->
                orderProduct.getOrderId() == todayOrder.getId()
        );
    }
}
