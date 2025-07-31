package kr.hhplus.be.server.balance.domain.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.balance.application.exception.BalanceErrorCode;
import kr.hhplus.be.server.common.exception.CustomException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"userId", "amount"})
public class Balance {

    private static final int MAX_POINT_LIMIT = 1000000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long userId;

    @Embedded
    private Amount amount;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Balance of(Long id, long userId, Amount amount) {
        return new Balance(id, userId, amount, LocalDateTime.now());
    }

    public Balance(Long id, long userId, Amount amount, LocalDateTime updatedAt) {

        if (userId < 0) {
            throw new CustomException(BalanceErrorCode.INVALID_NEGATIVE_USER_ID);
        }
        if (amount == null) {
            throw new CustomException(BalanceErrorCode.BALANCE_INFORMATION_REQUIRED);
        }

        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.updatedAt = updatedAt;
    }

    public int getAmount() {
        return amount.getValue();
    }

    public void charge(int value) {

        if (amount.getValue() + value > MAX_POINT_LIMIT) {
            throw new CustomException(BalanceErrorCode.RECHARGE_LIMIT_EXCEEDED);
        }

        this.amount = this.amount.add(value);
    }

    public void use(int value) {

        if (amount.getValue() - value < 0) {
            throw new CustomException(BalanceErrorCode.INSUFFICIENT_BALANCE);
        }

        this.amount = this.amount.sub(value);
    }
}
