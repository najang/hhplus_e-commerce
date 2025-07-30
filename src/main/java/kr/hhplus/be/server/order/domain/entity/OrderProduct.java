package kr.hhplus.be.server.order.domain.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.order.application.exception.OrderErrorCode;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private long productId;

    private String productName;

    private int sellPrice;

    private int count;

    public static OrderProduct of(Order order, Product product, int count) {

        if (order == null) {
            throw new CustomException(OrderErrorCode.ORDER_INFORMATION_REQUIRED);
        }
        if (product == null) {
            throw new CustomException(OrderErrorCode.PRODUCT_INFORMATION_REQUIRED);
        }

        return new OrderProduct(null, order.getId(), product.getId(), product.getName(), product.getPrice(), count);
    }

    public OrderProduct(Long id, long orderId, long productId, String productName, int sellPrice, int count) {

        if (orderId < 0) {
            throw new CustomException(OrderErrorCode.INVALID_NEGATIVE_ORDER_ID);
        }
        if (productId < 0) {
            throw new CustomException(OrderErrorCode.INVALID_NEGATIVE_PRODUCT_ID);
        }
        if (!StringUtils.hasText(productName)) {
            throw new CustomException(OrderErrorCode.PRODUCT_NAME_REQUIRED);
        }
        if (sellPrice < 0) {
            throw new CustomException(OrderErrorCode.INVALID_NEGATIVE_PRICE);
        }
        if (count <= 0) {
            throw new CustomException(OrderErrorCode.ORDER_QUANTITY_MUST_BE_POSITIVE);
        }

        this.id = id;
        this.productId = productId;
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

    public Long getOrderId() {
        return order.getId();
    }
}