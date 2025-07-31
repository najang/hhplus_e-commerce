package kr.hhplus.be.server.balance.application.service;

import kr.hhplus.be.server.balance.application.service.command.BalanceCommand;
import kr.hhplus.be.server.balance.domain.TransactionType;
import kr.hhplus.be.server.balance.domain.entity.Amount;
import kr.hhplus.be.server.balance.domain.entity.Balance;
import kr.hhplus.be.server.balance.domain.entity.PointHistory;
import kr.hhplus.be.server.balance.infrastructure.BalanceJpaRepository;
import kr.hhplus.be.server.balance.infrastructure.PointHistoryJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class BalanceServiceIntegrationTest {
    @Autowired
    private BalanceService balanceService;

    @Autowired
    private BalanceJpaRepository balanceJpaRepository;

    @Autowired
    private PointHistoryJpaRepository pointHistoryJpaRepository;

    @BeforeEach
    void setUp() {
        pointHistoryJpaRepository.deleteAll();
        balanceJpaRepository.deleteAll();
    }

    @DisplayName("잔액 충전")
    @Test
    void chargeBalance() {
        // given
        Balance balance = balanceJpaRepository.save(new Balance(null, 1L, Amount.of(10000), LocalDateTime.now()));

        BalanceCommand.BalanceChargeCommand command = new BalanceCommand.BalanceChargeCommand(balance.getUserId(), 10000);

        // when
        Balance result = balanceService.charge(command);

        // then
        assertThat(result.getAmount()).isEqualTo(20000);

        List<PointHistory> histories = pointHistoryJpaRepository.findAll();
        assertThat(histories).hasSize(1);
        assertThat(histories.get(0).getType()).isEqualTo(TransactionType.CHARGE);
        assertThat(histories.get(0).getAmount()).isEqualTo(Amount.of(10000));
    }

    @DisplayName("잔액 사용")
    @Test
    void useBalance() {
        // given
        Balance balance = balanceJpaRepository.save(new Balance(null, 1L, Amount.of(10000), LocalDateTime.now()));

        BalanceCommand.BalanceUseCommand command = new BalanceCommand.BalanceUseCommand(balance.getUserId(), 1L, 5000);

        // when
        Balance result = balanceService.use(command);

        // then
        assertThat(result.getAmount()).isEqualTo(5000);

        List<PointHistory> histories = pointHistoryJpaRepository.findAll();
        assertThat(histories).hasSize(1);
        assertThat(histories.get(0).getType()).isEqualTo(TransactionType.USE);
        assertThat(histories.get(0).getAmount()).isEqualTo(Amount.of(5000));
        assertThat(histories.get(0).getOrderId()).isEqualTo(1L);
    }
}
