package kr.hhplus.be.server.order.application.command;

import kr.hhplus.be.server.product.domain.entity.Product;

import java.util.List;

public class OrderCommand {

    public record OrderCreateCommand(
            long userId,
            List<OrderProductCreateCommand> orderItemCreateCommands
    ) {

        public static OrderCreateCommand of(long userId, List<OrderProductCreateCommand> orderItemCreateCommands) {
            return new OrderCreateCommand(userId, orderItemCreateCommands);
        }
    }

    public record OrderProductCreateCommand(
            Product product,
            int count
    ) {

        public static OrderProductCreateCommand of(Product product, int count) {
            return new OrderProductCreateCommand(product, count);
        }
    }
}