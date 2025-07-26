package kr.hhplus.be.server.order.presentation.controller;

import kr.hhplus.be.server.order.application.dto.request.OrderRequest;
import kr.hhplus.be.server.order.application.dto.response.OrderResponse;
import kr.hhplus.be.server.order.facade.OrderFacadeService;
import kr.hhplus.be.server.order.interfaces.api.OrderApiSpec;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController implements OrderApiSpec {

    private final OrderFacadeService orderFacadeService;

    public OrderController(OrderFacadeService orderFacadeService) {
        this.orderFacadeService = orderFacadeService;
    }

    @Override
    @PostMapping
    public ResponseEntity<OrderResponse.OrderDetailResponse> order(OrderRequest.OrderCreateRequest request) {
        OrderResponse.OrderDetailResponse response = OrderResponse.OrderDetailResponse.from(
                orderFacadeService.placeOrder(request.toCommand())
        );

        return ResponseEntity.ok(response);
    }
}
