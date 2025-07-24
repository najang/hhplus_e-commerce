package kr.hhplus.be.server.order.domain.repository;

import kr.hhplus.be.server.order.domain.entity.Order;
import kr.hhplus.be.server.order.domain.entity.OrderProduct;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository {
    Order saveOrder(Order order);

    List<OrderProduct> saveOrderProducts(List<OrderProduct> orderProduct);

    List<OrderProduct> findTodayOrderProducts();
}
