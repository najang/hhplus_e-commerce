package kr.hhplus.be.server.interfaces.balance.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/user")
public class BalanceApiController implements BalanceApiSpec {

    @GetMapping("/{userId}/balance")
    @Override
    public ResponseEntity<BalanceResponse> getUserBalance(Long userId) {
        return ResponseEntity.ok(BalanceResponse.of(1L, new BigDecimal("10000")));
    }

    @PostMapping("/charge")
    @Override
    public ResponseEntity<BalanceResponse> charge(Long id, BalanceRequest request) {
        return ResponseEntity.ok(BalanceResponse.of(2L, new BigDecimal("20000")));
    }
}
