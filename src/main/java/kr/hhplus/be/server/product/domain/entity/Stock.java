package kr.hhplus.be.server.product.domain.entity;

import jakarta.persistence.Embeddable;
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
            throw new IllegalArgumentException("재고는 음수가 될 수 없습니다.");
        }

        this.count = count;
    }

    public static Stock of(int count) {
        return new Stock(count);
    }

    public Stock decrease(int count) {
        if (this.count - count < 0) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        return Stock.of(this.count - count);
    }

    public Stock increase(int count) {
        return Stock.of(this.count + count);
    }
}