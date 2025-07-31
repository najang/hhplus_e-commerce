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
public class BalanceResponse {
        @Schema(title = "유저 잔액 정보 응답값")
        public record UserPointResponse(int amount, LocalDateTime updatedAt
        ) {
                public static UserPointResponse from(Balance balance) {
                        return new UserPointResponse(balance.getAmount(), balance.getUpdatedAt());
                }
        }
}