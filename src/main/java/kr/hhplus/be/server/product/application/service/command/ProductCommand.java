package kr.hhplus.be.server.product.application.service.command;

public class ProductCommand {
    public record StockDecreaseCommand(
            long productId,
            int count
    ) {
        public static StockDecreaseCommand of(long productId, int count) {
            return new StockDecreaseCommand(productId, count);
        }
    }

    public record StockIncreaseCommand(
            long productId,
            int count
    ) {
        public static StockIncreaseCommand of(long productId, int count) {
            return new StockIncreaseCommand(productId, count);
        }
    }
}
