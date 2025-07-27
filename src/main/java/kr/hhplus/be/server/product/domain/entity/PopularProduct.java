package kr.hhplus.be.server.product.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.order.domain.entity.OrderProduct;
import kr.hhplus.be.server.product.exception.ProductErrorCode;
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
            throw new CustomException(ProductErrorCode.INVALID_NEGATIVE_PRODUCT_ID);
        }
        if (price < 0) {
            throw new CustomException(ProductErrorCode.INVALID_NEGATIVE_PRICE);
        }
        if (orderDate == null) {
            throw new CustomException(ProductErrorCode.ORDER_DATE_INFORMATION_REQUIRED);
        }
        if (orderCount < 0) {
            throw new CustomException(ProductErrorCode.INVALID_NEGATIVE_ORDER_QUANTITY);
        }

        this.id = id;
        this.productId = productId;
        this.price = price;
        this.orderDate = orderDate;
        this.orderCount = orderCount;
        this.createdAt = createdAt;
    }
}