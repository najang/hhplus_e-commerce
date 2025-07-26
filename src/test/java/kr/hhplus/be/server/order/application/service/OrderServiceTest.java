package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.order.application.command.OrderCommand;
import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.order.domain.entity.OrderAmountInfo;
import kr.hhplus.be.server.order.domain.entity.OrderProduct;
import kr.hhplus.be.server.order.domain.entity.OrderStatus;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.domain.entity.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    OrderService orderService;

    OrderCommand.OrderCreateCommand command;
    Order order;
    List<OrderProduct> orderProducts;

    @BeforeEach
    void setUp() {

        List<Product> products = List.of(
                Product.of(1L, "상품명1", Stock.of(10), 10000),
                Product.of(2L, "상품명2", Stock.of(10), 20000)
        );

        List<OrderCommand.OrderProductCreateCommand> productCommands = List.of(
                OrderCommand.OrderProductCreateCommand.of(products.get(0), 1),
                OrderCommand.OrderProductCreateCommand.of(products.get(1), 2)
        );

        command = OrderCommand.OrderCreateCommand.of(1L, productCommands);

        order = new Order(1L, 1L, 1L, OrderStatus.COMPLETE, OrderAmountInfo.of(30000, 50000, 20000), LocalDateTime.now(), LocalDateTime.now());

        orderProducts = List.of(
                OrderProduct.of(order, products.get(0), 1),
                OrderProduct.of(order, products.get(1), 2));
    }

    @Nested
    class 주문_생성 {

        @Test
        void 주문_저장_레포지토리_1회_호출() {

            //given
            when(orderRepository.saveOrder(Order.of(command.userId())))
                    .thenReturn(order);

            when(orderRepository.saveOrderProducts(orderProducts))
                    .thenReturn(orderProducts);

            //when
            orderService.createOrder(command);

            //then
            verify(orderRepository, times(1)).saveOrder(Order.of(command.userId()));
        }

        @Test
        void 주문_상품_목록_저장_레포지토리_1회_호출() {

            //given
            when(orderRepository.saveOrder(Order.of(command.userId())))
                    .thenReturn(order);

            when(orderRepository.saveOrderProducts(orderProducts))
                    .thenReturn(orderProducts);

            //when
            orderService.createOrder(command);

            //then
            verify(orderRepository, times(1)).saveOrderProducts(orderProducts);
        }

        @Test
        void 주문_저장_실패_시_주문_상품_목록_저장_레포지토리_0회_호출() {

            //given
            when(orderRepository.saveOrder(Order.of(command.userId())))
                    .thenThrow(RuntimeException.class);

            //when
            ;

            //then
            assertThatThrownBy(() -> orderService.createOrder(command))
                    .isInstanceOf(RuntimeException.class);

            verify(orderRepository, times(0)).saveOrderProducts(orderProducts);
        }
    }
}
