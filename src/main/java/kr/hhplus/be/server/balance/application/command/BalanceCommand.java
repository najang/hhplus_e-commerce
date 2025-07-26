package kr.hhplus.be.server.balance.application.command;

public class BalanceCommand {
    public record BalanceChargeCommand(long userId, int amount) {
    }

    public record BalanceUseCommand(long userId, long orderId, int amount) {
        public static BalanceUseCommand of(long userId, long orderId, int amount) {
            return new BalanceUseCommand(userId, orderId, amount);
        }
    }
}
