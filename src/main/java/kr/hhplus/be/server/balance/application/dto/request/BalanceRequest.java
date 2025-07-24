package kr.hhplus.be.server.balance.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.balance.application.command.BalanceCommand;

public class BalanceRequest {
        @Schema(title = "유저 잔액 충전 요청값")
        public record BalanceChargeRequest(@Schema(description = "유저식별자", example = "1") @Positive long userId,
                                         @Schema(description = "충전 금액", example = "1000") @Positive int amount
        ) {
                public BalanceCommand.BalanceChargeCommand toCommand() {
                        return new BalanceCommand.BalanceChargeCommand(userId, amount);
                }
        }
}