package kr.hhplus.be.server.interfaces.order.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/order")
public class OrderApiController implements OrderApiSpec {

    @Override
    @PostMapping
    public ResponseEntity<OrderResponse> order(OrderRequest request) {
        return ResponseEntity.ok(
            OrderResponse.of(4L, new BigDecimal("100000"), new BigDecimal("2000"), "2025-08-01 00:00:00")
        );
    }
}
