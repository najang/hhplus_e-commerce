package kr.hhplus.be.server.interfaces.balance.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Schema(title = "유저 충전 금액 응답값")
public class BalanceResponse {

        private Long userId;
        private BigDecimal amount;

        @Builder
        private BalanceResponse(Long userId, BigDecimal amount) {
                this.userId = userId;
                this.amount = amount;
        }

        public static BalanceResponse of(Long userId, BigDecimal amount) {
                return new BalanceResponse(userId, amount);
        }
}