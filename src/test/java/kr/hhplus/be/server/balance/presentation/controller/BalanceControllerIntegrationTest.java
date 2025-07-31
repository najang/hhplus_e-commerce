package kr.hhplus.be.server.balance.presentation.controller;

import kr.hhplus.be.server.balance.application.dto.request.BalanceRequest;
import kr.hhplus.be.server.balance.application.dto.response.BalanceResponse;
import kr.hhplus.be.server.balance.domain.TransactionType;
import kr.hhplus.be.server.balance.domain.entity.Amount;
import kr.hhplus.be.server.balance.domain.entity.Balance;
import kr.hhplus.be.server.balance.domain.entity.PointHistory;
import kr.hhplus.be.server.balance.infrastructure.BalanceJpaRepository;
import kr.hhplus.be.server.balance.infrastructure.PointHistoryJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class BalanceControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BalanceJpaRepository balanceJpaRepository;

    @Autowired
    private PointHistoryJpaRepository pointHistoryJpaRepository;

    @BeforeEach
    void setUp() {
        pointHistoryJpaRepository.deleteAll();
        balanceJpaRepository.deleteAll();
    }

    @DisplayName("유저 잔액 조회")
    @Test
    void getUserBalance() {

        // given
        Balance balance = balanceJpaRepository.save(new Balance(null, 1L, Amount.of(10000), LocalDateTime.now()));

        // when
        ResponseEntity<BalanceResponse.UserPointResponse> response = restTemplate.getForEntity(
                "/api/v1/user/" + balance.getUserId() + "/balance",
                BalanceResponse.UserPointResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().amount()).isEqualTo(10000);
    }

    @DisplayName("양수가 아닌 유저식별자로 유저 잔액 조회")
    @ParameterizedTest
    @ValueSource(longs = {-100L, -10L, -3L, -2L, -1L, 0L})
    void getUserBalanceOrThrowIfInvalidId(long userId) {

        // when
        ResponseEntity<BalanceResponse.UserPointResponse> response = restTemplate.getForEntity(
                "/api/v1/user/" + + userId + "/balance",
                BalanceResponse.UserPointResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("잔액 충전")
    @Test
    void chargeBalance() {
        // given
        Balance balance = balanceJpaRepository.save(new Balance(null, 1L, Amount.of(10000), LocalDateTime.now()));
        Long userId = balance.getUserId();
        int chargeAmount = 10000;

        BalanceRequest.BalanceChargeRequest request = new BalanceRequest.BalanceChargeRequest(userId, chargeAmount);

        // when
        ResponseEntity<BalanceResponse.UserPointResponse> response = restTemplate.postForEntity(
                "/api/v1/user/charge",
                request,
                BalanceResponse.UserPointResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().amount()).isEqualTo(20000);

        List<PointHistory> histories = pointHistoryJpaRepository.findAll();
        assertThat(histories).hasSize(1);
        assertThat(histories.get(0).getAmount()).isEqualTo(Amount.of(chargeAmount));
        assertThat(histories.get(0).getType()).isEqualTo(TransactionType.CHARGE);
    }

    @DisplayName("양수가 아닌 유저식별자로 잔액 충전")
    @ParameterizedTest
    @ValueSource(longs = {-100L, -10L, -3L, -2L, -1L, 0L})
    void chargeBalanceOrThrowIfInvalidUserId(long userId) {

        // given
        BalanceRequest.BalanceChargeRequest request = new BalanceRequest.BalanceChargeRequest(userId, 10000);

        // when
        ResponseEntity<BalanceResponse.UserPointResponse> response = restTemplate.postForEntity(
                "/api/v1/user/charge",
                request,
                BalanceResponse.UserPointResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("양수가 아닌 충전금액으로 잔액 충전")
    @ParameterizedTest
    @ValueSource(ints = {-100, -10, -3, -2, -1, 0})
    void chargeBalanceOrThrowIfInvalidAmount(int amount) {

        // given
        BalanceRequest.BalanceChargeRequest request = new BalanceRequest.BalanceChargeRequest(1L, amount);

        // when
        ResponseEntity<BalanceResponse.UserPointResponse> response = restTemplate.postForEntity(
                "/api/v1/user/charge",
                request,
                BalanceResponse.UserPointResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
