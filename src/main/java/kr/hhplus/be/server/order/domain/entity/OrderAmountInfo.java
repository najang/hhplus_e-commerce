package kr.hhplus.be.server.order.domain.entity;

import jakarta.persistence.Embeddable;
import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.order.application.exception.OrderErrorCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@Embeddable
public class OrderAmountInfo {

    private final int totalAmount;

    private final int productTotalAmount;

    private final int discountAmount;

    protected OrderAmountInfo() {
        this.totalAmount = 0;
        this.productTotalAmount = 0;
        this.discountAmount = 0;
    }

    public static OrderAmountInfo of() {
        return new OrderAmountInfo();
    }

    public static OrderAmountInfo of(int totalAmount, int productTotalAmount, int discountAmount) {
        return new OrderAmountInfo(totalAmount, productTotalAmount, discountAmount);
    }

    public OrderAmountInfo(int totalAmount, int productTotalAmount, int discountAmount) {

        if (totalAmount < 0) {
            throw new CustomException(OrderErrorCode.INVALID_NEGATIVE_TOTAL_PRICE);
        }
        if (productTotalAmount < 0) {
            throw new CustomException(OrderErrorCode.INVALID_NEGATIVE_TOTAL_PRODUCT_PRICE);
        }
        if (discountAmount < 0) {
            throw new CustomException(OrderErrorCode.INVALID_NEGATIVE_DISCOUNT_PRICE);
        }
        if (totalAmount != productTotalAmount - discountAmount) {
            throw new CustomException(OrderErrorCode.INVALID_PRICE_CALCULATION);
        }

        this.totalAmount = totalAmount;
        this.productTotalAmount = productTotalAmount;
        this.discountAmount = discountAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderAmountInfo that = (OrderAmountInfo) o;
        return totalAmount == that.totalAmount && productTotalAmount == that.productTotalAmount && discountAmount == that.discountAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalAmount, productTotalAmount, discountAmount);
    }

    public OrderAmountInfo applyDiscount(int discountAmount) {
        return OrderAmountInfo.of(this.totalAmount - discountAmount, this.productTotalAmount, this.discountAmount + discountAmount);
    }
}