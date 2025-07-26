package kr.hhplus.be.server.balance.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.balance.domain.entity.Balance;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(title = "유저 충전 금액 응답값")
public class BalanceResponse {

        private Long userId;
        private int amount;
        private LocalDateTime updatedAt;

        @Builder
        private BalanceResponse(Long userId, int amount, LocalDateTime updatedAt) {
                this.userId = userId;
                this.amount = amount;
                this.updatedAt = updatedAt;
        }

        public static BalanceResponse from(Balance balance) {
                return new BalanceResponse(balance.getUserId(), balance.getAmount(), balance.getUpdatedAt());
        }
}