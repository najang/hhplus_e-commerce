package kr.hhplus.be.server.order.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.product.domain.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Order order;

    private long orderId;

    private long productId;

    private String productName;

    private int sellPrice;

    private int count;

    public static OrderProduct of(Order order, Product product, int count) {

        if (order == null) {
            throw new IllegalArgumentException("주문 정보가 필요합니다.");
        }
        if (product == null) {
            throw new IllegalArgumentException("상품 정보가 필요합니다.");
        }

        return new OrderProduct(null, order.getId(), product.getId(), product.getName(), product.getPrice(), count);
    }

    public OrderProduct(Long id, long orderId, long itemId, String productName, int sellPrice, int count) {

        if (orderId < 0) {
            throw new IllegalArgumentException("주문식별자는 음수일 수 없습니다.");
        }
        if (itemId < 0) {
            throw new IllegalArgumentException("상품식별자는 음수일 수 없습니다.");
        }
        if (!StringUtils.hasText(productName)) {
            throw new IllegalArgumentException("상품명을 입력해주세요.");
        }
        if (sellPrice < 0) {
            throw new IllegalArgumentException("상품 가격은 음수일 수 없습니다.");
        }
        if (count <= 0) {
            throw new IllegalArgumentException("주문 수량은 양수여야 합니다.");
        }

        this.id = id;
        this.orderId = orderId;
        this.productId = itemId;
        this.productName = productName;
        this.sellPrice = sellPrice;
        this.count = count;
    }

    public int getOrderProductPrice() {
        return sellPrice * count;
    }

    public LocalDate getOrderDate() {
        return order.getCreatedAt().toLocalDate();
    }
}