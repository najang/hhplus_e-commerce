package kr.hhplus.be.server.order.domain.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long userId;

    private Long couponIssueId;

    private OrderStatus orderStatus;

    @Embedded
    private OrderAmountInfo orderAmountInfo;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Order of(long userId) {
        return new Order(null, userId, null, OrderStatus.COMPLETE, OrderAmountInfo.of(), LocalDateTime.now(), LocalDateTime.now());
    }

    public Order(Long id, long userId, Long couponIssueId, OrderStatus orderStatus, OrderAmountInfo orderAmountInfo, LocalDateTime createdAt, LocalDateTime updatedAt) {

        if (userId < 0) {
            throw new IllegalArgumentException("유저식별자는 음수일 수 없습니다.");
        }
        if (orderStatus == null) {
            throw new IllegalArgumentException("주문 상태 정보가 필요합니다.");
        }
        if (orderAmountInfo == null) {
            throw new IllegalArgumentException("주문 가격 정보가 필요합니다.");
        }

        this.id = id;
        this.userId = userId;
        this.couponIssueId = couponIssueId;
        this.orderStatus = orderStatus;
        this.orderAmountInfo = orderAmountInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void calculateOrderAmount(List<OrderProduct> orderProducts) {

        if (orderProducts == null) {
            throw new IllegalArgumentException("주문 상품 정보가 필요합니다.");
        }

        int itemTotalAmount = orderProducts.stream()
                .mapToInt(OrderProduct::getOrderProductPrice)
                .sum();
        int discountAmount = 0;
        int totalAmount = itemTotalAmount - discountAmount;

        this.orderAmountInfo = OrderAmountInfo.of(totalAmount, itemTotalAmount, discountAmount);
    }

    public void applyDiscount(int discountAmount) {
        this.orderAmountInfo = orderAmountInfo.applyDiscount(discountAmount);
    }
}
