package kr.hhplus.be.server.interfaces.balance.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "유저 잔액 충전 요청값")
public class BalanceRequest {

        @NotNull(message = "잔액은 필수입니다.")
        @Positive(message = "잔액은 양수여야 합니다.")
        private Long amount;

        @Builder
        private BalanceRequest(Long amount) {
                this.amount = amount;
        }

        public static BalanceRequest of(Long amount) {
                return new BalanceRequest(amount);
        }
}