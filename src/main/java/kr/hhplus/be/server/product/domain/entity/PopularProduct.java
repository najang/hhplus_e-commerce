package kr.hhplus.be.server.product.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.order.domain.entity.OrderProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class PopularProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long productId;

    private int price;

    private LocalDate orderDate;

    private int orderCount;

    @CreatedDate
    private LocalDateTime createdAt;

    public static PopularProduct of(OrderProduct orderProduct) {
        return PopularProduct.of(orderProduct.getProductId(), orderProduct.getSellPrice(), LocalDate.now(), orderProduct.getCount(), LocalDateTime.now());
    }

    public static PopularProduct of(long productId, int price, LocalDate orderDate, int orderCount, LocalDateTime createdAt) {
        return new PopularProduct(null, productId, price, orderDate, orderCount, LocalDateTime.now());
    }

    public PopularProduct(Long id, long productId, int price, LocalDate orderDate, int orderCount, LocalDateTime createdAt) {

        if (productId < 0) {
            throw new IllegalArgumentException("상품식별자는 음수일 수 없습니다.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("상품 가격은 음수일 수 없습니다.");
        }
        if (orderDate == null) {
            throw new IllegalArgumentException("주문날짜 정보가 필요합니다.");
        }
        if (orderCount < 0) {
            throw new IllegalArgumentException("주문 수량은 음수일 수 없습니다.");
        }

        this.id = id;
        this.productId = productId;
        this.price = price;
        this.orderDate = orderDate;
        this.orderCount = orderCount;
        this.createdAt = createdAt;
    }
}