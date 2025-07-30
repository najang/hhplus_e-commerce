package kr.hhplus.be.server.order.infrastructure;

import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.order.domain.entity.OrderProduct;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderProductJpaRepository orderProductJpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, OrderProductJpaRepository orderProductJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderProductJpaRepository = orderProductJpaRepository;
    }

    @Override
    public Order saveOrder(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public List<OrderProduct> saveOrderProducts(List<OrderProduct> orderProduct) {
        return orderProductJpaRepository.saveAll(orderProduct);
    }

    @Override
    public List<OrderProduct> findTodayOrderProducts() {
        return List.of();
    }
}
