package kr.hhplus.be.server.order.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.order.facade.OrderFacadeCommand;

import java.util.List;


public class OrderRequest {

    @Schema(title = "주문 결제 요청값")
    public record OrderCreateRequest(
            @Schema(description = "유저식별자", example = "1")
            @Positive
            long userId,
            @Schema(description = "쿠폰식별자", example = "1")
            Long couponId,
            @Schema(description = "주문 상품 정보 목록", example = "[1, 2, 3]")
            @NotNull
            List<OrderProductCreateRequest> products
    ) {
        public OrderFacadeCommand.OrderCreateFacadeCommand toCommand() {

            List<OrderFacadeCommand.OrderProductCreateFacadeCommand> productCommands = products.stream()
                    .map(OrderProductCreateRequest::toCommand)
                    .toList();

            return OrderFacadeCommand.OrderCreateFacadeCommand.of(userId, couponId, productCommands);
        }
    }

    @Schema(title = "주문 상품 요청값")
    public record OrderProductCreateRequest(
            @Schema(description = "상품식별자", example = "1")
            @Positive
            long productId,
            @Schema(description = "주문수량", example = "1")
            @Positive
            int count
    ) {
        public OrderFacadeCommand.OrderProductCreateFacadeCommand toCommand() {
            return OrderFacadeCommand.OrderProductCreateFacadeCommand.of(productId, count);
        }
    }
}