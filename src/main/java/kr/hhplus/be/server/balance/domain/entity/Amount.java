package kr.hhplus.be.server.balance.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Amount {

    private final int value;

    protected Amount() {
        this.value = 0;
    }

    public static Amount of(int value) {
        return new Amount(value);
    }

    public Amount(int value) {

        if (value < 0) {
            throw new IllegalArgumentException("금액은 음수일 수 없습니다.");
        }

        this.value = value;
    }

    public Amount add(int value) {
        return Amount.of(this.value + value);
    }

    public Amount sub(int value) {
        return Amount.of(this.value - value);
    }
}