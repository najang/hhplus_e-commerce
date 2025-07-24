package kr.hhplus.be.server.order.facade;

import kr.hhplus.be.server.balance.application.command.BalanceCommand;
import kr.hhplus.be.server.coupon.command.CouponCommand;
import kr.hhplus.be.server.order.application.command.OrderCommand;
import kr.hhplus.be.server.order.domain.entity.OrderInfo;
import kr.hhplus.be.server.product.application.command.ProductCommand;
import kr.hhplus.be.server.product.domain.entity.Product;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderFacadeCommand {

    public record OrderCreateFacadeCommand(
            long userId,
            Long couponId,
            List<OrderProductCreateFacadeCommand> orderProducts
    ) {

        public static OrderCreateFacadeCommand of(long userId, Long couponId,List<OrderProductCreateFacadeCommand> productCommands) {
            return new OrderCreateFacadeCommand(userId, couponId,productCommands);
        }

        public List<ProductCommand.StockDecreaseCommand> toStockDecreaseCommands() {
            return orderProducts.stream()
                    .map(OrderProductCreateFacadeCommand::toStockDecreaseCommand)
                    .toList();
        }

        public OrderCommand.OrderCreateCommand toOrderCreateCommand(List<Product> products) {

            Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));

            List<OrderCommand.OrderProductCreateCommand> orderProductCreateCommands = orderProducts.stream()
                    .map(orderProduct -> orderProduct.toOrderProductCreateCommand(productMap.get(orderProduct.productId)))
                    .toList();

            return OrderCommand.OrderCreateCommand.of(userId, orderProductCreateCommands);
        }

        public BalanceCommand.PointUseCommand toPointUseCommand(OrderInfo orderInfo) {
            return BalanceCommand.PointUseCommand.of(userId, orderInfo.order().getId(), orderInfo.getTotalAmount());
        }

        public CouponCommand.CouponApplyCommand toCouponApplyCommand(OrderInfo orderInfo) {
            return CouponCommand.CouponApplyCommand.of(orderInfo.order(), couponId);
        }
    }

    public record OrderProductCreateFacadeCommand(
            long productId,
            int count
    ) {

        public static OrderProductCreateFacadeCommand of(long productId, int count) {
            return new OrderProductCreateFacadeCommand(productId, count);
        }

        public ProductCommand.StockDecreaseCommand toStockDecreaseCommand() {
            return ProductCommand.StockDecreaseCommand.of(productId, count);
        }

        public OrderCommand.OrderProductCreateCommand toOrderProductCreateCommand(Product product) {
            return OrderCommand.OrderProductCreateCommand.of(product, count);
        }
    }
}