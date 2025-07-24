package kr.hhplus.be.server.order.domain.entity;

import jakarta.persistence.Embeddable;
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
            throw new IllegalArgumentException("총 가격은 음수일 수 없습니다.");
        }
        if (productTotalAmount < 0) {
            throw new IllegalArgumentException("총 상품 가격은 음수일 수 없습니다.");
        }
        if (discountAmount < 0) {
            throw new IllegalArgumentException("할인 가격은 음수일 수 없습니다.");
        }
        if (totalAmount != productTotalAmount - discountAmount) {
            throw new IllegalArgumentException("가격 계산이 올바르지 않습니다.");
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