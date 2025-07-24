package kr.hhplus.be.server.product.application.command;

public class ProductCommand {
    public record StockDecreaseCommand(
            long productId,
            int count
    ) {
        public static StockDecreaseCommand of(long productId, int count) {
            return new StockDecreaseCommand(productId, count);
        }
    }
}
