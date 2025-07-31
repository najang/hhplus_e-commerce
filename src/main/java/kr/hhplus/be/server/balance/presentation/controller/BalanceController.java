package kr.hhplus.be.server.balance.presentation.controller;

import kr.hhplus.be.server.balance.application.dto.request.BalanceRequest;
import kr.hhplus.be.server.balance.application.dto.response.BalanceResponse;
import kr.hhplus.be.server.balance.application.service.BalanceService;
import kr.hhplus.be.server.balance.presentation.interfaces.api.BalanceApiSpec;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class BalanceController implements BalanceApiSpec {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/{userId}/balance")
    @Override
    public ResponseEntity<BalanceResponse.UserPointResponse> getUserBalance(@PathVariable Long userId) {
        BalanceResponse.UserPointResponse response = BalanceResponse.UserPointResponse.from(balanceService.findByUserId(userId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/charge")
    @Override
    public ResponseEntity<BalanceResponse.UserPointResponse> charge(BalanceRequest.BalanceChargeRequest request) {
        BalanceResponse.UserPointResponse response = BalanceResponse.UserPointResponse.from(balanceService.charge(request.toCommand()));
        return ResponseEntity.ok(response);
    }
}
