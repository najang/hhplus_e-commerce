package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.order.application.command.OrderCommand;
import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.order.domain.entity.OrderInfo;
import kr.hhplus.be.server.order.domain.entity.OrderProduct;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderInfo createOrder(OrderCommand.OrderCreateCommand command) {

        Order order = orderRepository.saveOrder(Order.of(command.userId()));

        List<OrderProduct> orderProducts = command.orderItemCreateCommands().stream()
                .map(orderItemCreateCommand -> OrderProduct.of(
                        order,
                        orderItemCreateCommand.product(),
                        orderItemCreateCommand.count()))
                .toList();

        order.calculateOrderAmount(orderProducts);

        orderProducts = orderRepository.saveOrderProducts(orderProducts);

        return OrderInfo.of(order, orderProducts);
    }

    public List<OrderProduct> findTodayOrderProducts() {
        return orderRepository.findTodayOrderProducts();
    }
}
