package kr.hhplus.be.server.balance.domain.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
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
            throw new IllegalArgumentException("유저식별자는 음수일 수 없습니다.");
        }
        if (amount == null) {
            throw new IllegalArgumentException("잔액 정보가 필요합니다.");
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
            throw new IllegalArgumentException("최대 한도를 초과하여 충전할 수 없습니다.");
        }

        this.amount = this.amount.add(value);
    }

    public void use(int value) {

        if (amount.getValue() - value < 0) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        this.amount = this.amount.sub(value);
    }
}
