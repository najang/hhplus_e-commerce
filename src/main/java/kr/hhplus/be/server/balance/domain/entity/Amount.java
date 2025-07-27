package kr.hhplus.be.server.balance.domain.entity;

import jakarta.persistence.Embeddable;
import kr.hhplus.be.server.balance.application.exception.BalanceErrorCode;
import kr.hhplus.be.server.common.exception.CustomException;
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
            throw new CustomException(BalanceErrorCode.INVALID_NEGATIVE_AMOUNT);
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