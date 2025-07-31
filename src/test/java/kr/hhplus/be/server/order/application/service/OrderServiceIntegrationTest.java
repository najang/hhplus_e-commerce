package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.order.application.service.command.OrderCommand;
import kr.hhplus.be.server.order.domain.entity.OrderAmountInfo;
import kr.hhplus.be.server.order.domain.entity.OrderInfo;
import kr.hhplus.be.server.order.infrastructure.OrderJpaRepository;
import kr.hhplus.be.server.order.infrastructure.OrderProductJpaRepository;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.domain.entity.Stock;
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
public class OrderServiceIntegrationTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private OrderProductJpaRepository orderItemJpaRepository;

    @BeforeEach
    void setUp() {
        orderItemJpaRepository.deleteAll();
        orderJpaRepository.deleteAll();
    }

    @DisplayName("주문 생성 후 주문 항목 저장")
    @Test
    void saveOrderProducts() {
        // given
        OrderCommand.OrderCreateCommand command = new OrderCommand.OrderCreateCommand(1L, List.of(
            new OrderCommand.OrderProductCreateCommand(new Product(1L, "상품명1", Stock.of(1000), 100, LocalDateTime.now(), LocalDateTime.now()), 2),
            new OrderCommand.OrderProductCreateCommand(new Product(2L, "상품명2", Stock.of(1000), 100, LocalDateTime.now(), LocalDateTime.now()), 1)
        ));

        // when
        OrderInfo orderInfo = orderService.createOrder(command);

        // then
        assertThat(orderJpaRepository.findAll().get(0)).isEqualTo(orderInfo.order());
        assertThat(orderItemJpaRepository.findAll()).isEqualTo(orderInfo.orderProducts());
    }

    @DisplayName("주문 생성시 총액 계산")
    @Test
    void calculateTotalAmount() {
        // given
        OrderCommand.OrderCreateCommand command = new OrderCommand.OrderCreateCommand(1L, List.of(
                new OrderCommand.OrderProductCreateCommand(new Product(1L, "상품명", Stock.of(1000), 10000, LocalDateTime.now(), LocalDateTime.now()), 2),
                new OrderCommand.OrderProductCreateCommand(new Product(2L, "상품명", Stock.of(1000), 20000, LocalDateTime.now(), LocalDateTime.now()), 1)
        ));

        // when
        OrderInfo orderInfo = orderService.createOrder(command);

        // then
        assertThat(orderInfo.order().getOrderAmountInfo()).isEqualTo(OrderAmountInfo.of(40000, 40000, 0));
    }
}
