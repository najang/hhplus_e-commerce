package kr.hhplus.be.server.balance.domain.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.balance.application.exception.BalanceErrorCode;
import kr.hhplus.be.server.balance.domain.TransactionType;
import kr.hhplus.be.server.common.exception.CustomException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long pointId;

    private Long orderId;

    private Amount amount;

    @Enumerated
    TransactionType type;

    @CreatedDate
    LocalDateTime createdAt;

    public static PointHistory ofCharge(long pointId, int amount) {
        return PointHistory.ofCharge(null, pointId, Amount.of(amount));
    }

    public static PointHistory ofCharge(Long id, long pointId, Amount amount) {
        return new PointHistory(id, pointId, null, amount, TransactionType.CHARGE, LocalDateTime.now());
    }

    public static PointHistory ofUse(long pointId, long orderId, int amount) {
        return PointHistory.ofUse(null, pointId, orderId, Amount.of(amount));
    }

    public static PointHistory ofUse(Long id, long pointId, long orderId, Amount amount) {
        return new PointHistory(id, pointId, orderId, amount, TransactionType.USE, LocalDateTime.now());
    }

    public PointHistory(Long id, long pointId, Long orderId, Amount amount, TransactionType type, LocalDateTime createdAt) {

        if (pointId < 0) {
            throw new CustomException(BalanceErrorCode.	INVALID_NEGATIVE_POINT_ID);
        }
        if (amount == null) {
            throw new CustomException(BalanceErrorCode.	AMOUNT_INFORMATION_REQUIRED);
        }
        if (type == null) {
            throw new CustomException(BalanceErrorCode.	TRANSACTION_TYPE_INFORMATION_REQUIRED);
        }

        this.id = id;
        this.pointId = pointId;
        this.orderId = orderId;
        this.amount = amount;
        this.type = type;
        this.createdAt = createdAt;
    }
}