package kr.hhplus.be.server.product.domain.entity;

import jakarta.persistence.Embeddable;
import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.product.application.exception.ProductErrorCode;
import lombok.Getter;

@Embeddable
@Getter
public class Stock {
    private final int count;

    protected Stock() {
        this.count = 0;
    }

    public Stock(int count) {
        if (count < 0) {
            throw new CustomException(ProductErrorCode.INVALID_NEGATIVE_STOCK);
        }

        this.count = count;
    }

    public static Stock of(int count) {
        return new Stock(count);
    }

    public Stock decrease(int count) {
        if (this.count - count < 0) {
            throw new CustomException(ProductErrorCode.INSUFFICIENT_STOCK);
        }

        return Stock.of(this.count - count);
    }

    public Stock increase(int count) {
        return Stock.of(this.count + count);
    }
}