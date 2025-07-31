package kr.hhplus.be.server.order.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.order.domain.entity.OrderProduct;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static kr.hhplus.be.server.order.domain.entity.QOrder.*;
import static kr.hhplus.be.server.order.domain.entity.QOrderProduct.*;

@Repository
public class OrderQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public OrderQuerydslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<OrderProduct> findTodayOrderProducts() {
        return queryFactory.selectFrom(orderProduct)
                .innerJoin(order).on(order.id.eq(orderProduct.orderId))
                .where(order.createdAt.between(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)))
                .fetch();
    }
}