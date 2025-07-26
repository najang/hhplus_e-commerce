package kr.hhplus.be.server.order.facade;

import kr.hhplus.be.server.balance.application.service.BalanceService;
import kr.hhplus.be.server.coupon.application.service.CouponService;
import kr.hhplus.be.server.order.application.dto.result.OrderResult;
import kr.hhplus.be.server.order.application.service.OrderService;
import kr.hhplus.be.server.order.domain.entity.OrderInfo;
import kr.hhplus.be.server.product.application.service.ProductService;
import kr.hhplus.be.server.product.domain.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderFacadeService {

    private final ProductService productService;
    private final BalanceService balanceService;
    private final OrderService orderService;
    private final CouponService couponService;

    public OrderFacadeService(ProductService productService, BalanceService balanceService, OrderService orderService, CouponService couponService) {
        this.productService = productService;
        this.balanceService = balanceService;
        this.orderService = orderService;
        this.couponService = couponService;
    }

    @Transactional
    public OrderResult.OrderCreateResult placeOrder(OrderFacadeCommand.OrderCreateFacadeCommand command) {

        List<Product> products = productService.decreaseStocks(command.toStockDecreaseCommands());

        OrderInfo orderInfo = orderService.createOrder(command.toOrderCreateCommand(products));

        if (command.couponId() != null) {
            int discountAmount = couponService.applyCoupon(command.toCouponApplyCommand(orderInfo));
            orderInfo.applyDiscount(discountAmount);
        }

        balanceService.use(command.toPointUseCommand(orderInfo));

        return OrderResult.OrderCreateResult.from(orderInfo);
    }
}