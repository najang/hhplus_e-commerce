package kr.hhplus.be.server.balance.infrastructure;

import kr.hhplus.be.server.balance.domain.TransactionType;
import kr.hhplus.be.server.balance.domain.entity.Amount;
import kr.hhplus.be.server.balance.domain.entity.Balance;
import kr.hhplus.be.server.balance.domain.entity.PointHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class BalanceRepositoryImplTest {
    @Autowired
    private BalanceRepositoryImpl balanceRepository;

    @Autowired
    private BalanceJpaRepository balanceJpaRepository;

    @Autowired
    private PointHistoryJpaRepository pointHistoryJpaRepository;

    @BeforeEach
    void setUp() {
        pointHistoryJpaRepository.deleteAll();
        balanceJpaRepository.deleteAll();
    }

    @DisplayName("유저 식별자로 잔액 조회")
    @Test
    void findBalanceByUserId() {
        //given
        Balance balance = new Balance(null, 1L, Amount.of(1000), LocalDateTime.now());
        balanceJpaRepository.save(balance);

        //when
        Balance result = balanceRepository.findByUserId(1L);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(balance);
    }

    @DisplayName("포인트 내역 저장")
    @Test
    void savePointHistory() {
        //given
        PointHistory pointHistory = new PointHistory(null, 1L, 1L, Amount.of(10000), TransactionType.CHARGE, LocalDateTime.now());

        //when
        PointHistory result = balanceRepository.savePointHistory(pointHistory);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result).isEqualTo(pointHistory);
        assertThat(pointHistoryJpaRepository.findAll()).hasSize(1);
    }
}
