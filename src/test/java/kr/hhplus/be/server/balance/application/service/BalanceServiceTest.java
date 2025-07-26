package kr.hhplus.be.server.balance.application.service;

import kr.hhplus.be.server.balance.application.command.BalanceCommand;
import kr.hhplus.be.server.balance.domain.entity.Amount;
import kr.hhplus.be.server.balance.domain.entity.Balance;
import kr.hhplus.be.server.balance.domain.entity.PointHistory;
import kr.hhplus.be.server.balance.domain.repository.BalanceRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    BalanceRepository balanceRepository;
    @InjectMocks
    BalanceService balanceService;

    @Nested
    class 유저_잔액_조회 {

        @Test
        void 유저_잔액_조회_레포지토리를_1회_호출() {

            //given
            when(balanceRepository.findByUserId(1L))
                    .thenReturn(Balance.of(1L, 1L, Amount.of(1000)));

            //when
            balanceService.findByUserId(1L);

            //then
            verify(balanceRepository, times(1)).findByUserId(1L);
        }
    }

    @Nested
    class 유저_잔액_충전 {

        @Test
        void 유저_잔액_조회_레포지토리를_1회_호출() {

            //given
            when(balanceRepository.findByUserId(1L))
                    .thenReturn(Balance.of(1L, 1L, Amount.of(1000)));

            BalanceCommand.BalanceChargeCommand command = new BalanceCommand.BalanceChargeCommand(1L, 1000);

            //when
            balanceService.charge(command);

            //then
            verify(balanceRepository, times(1)).findByUserId(1L);
        }

        @Test
        void 포인트_내역_저장_레포지토리를_1회_호출() {

            //given
            when(balanceRepository.findByUserId(1L))
                    .thenReturn(Balance.of(1L, 1L, Amount.of(1000)));

            BalanceCommand.BalanceChargeCommand command = new BalanceCommand.BalanceChargeCommand(1L, 1000);

            //when
            balanceService.charge(command);

            //then
            verify(balanceRepository, times(1)).savePointHistory(PointHistory.ofCharge(1L, 1000));
        }

        @Test
        void 포인트_충전에_실패하면_포인트_내역_저장_레포지토리를_0회_호출() {

            //given
            when(balanceRepository.findByUserId(1L))
                    .thenReturn(Balance.of(1L, 1L, Amount.of(1000)));

            BalanceCommand.BalanceChargeCommand command = new BalanceCommand.BalanceChargeCommand(1L, -2000);

            //when, then
            assertThatThrownBy(() -> balanceService.charge(command))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(balanceRepository, times(0)).savePointHistory(any());
        }

        @Test
        void 포인트_잔액에_충전_금액_더한_포인트_반환() {

            //given
            when(balanceRepository.findByUserId(1L))
                    .thenReturn(Balance.of(1L, 1L, Amount.of(1000)));

            BalanceCommand.BalanceChargeCommand command = new BalanceCommand.BalanceChargeCommand(1L, 1000);

            //when
            Balance result = balanceService.charge(command);

            //then
            assertThat(result).isEqualTo(Balance.of(1L, 1L, Amount.of(2000)));
        }
    }

    @Nested
    class 유저_잔액_차감 {

        @Test
        void 유저_잔액_조회_레포지토리를_1회_호출() {

            //given
            when(balanceRepository.findByUserId(1L))
                    .thenReturn(Balance.of(1L, 1L, Amount.of(1000)));

            BalanceCommand.PointUseCommand command = new BalanceCommand.PointUseCommand(1L, 1L, 1000);

            //when
            balanceService.use(command);

            //then
            verify(balanceRepository, times(1)).findByUserId(1L);
        }

        @Test
        void 포인트_내역_저장_레포지토리를_1회_호출() {

            //given
            when(balanceRepository.findByUserId(1L))
                    .thenReturn(Balance.of(1L, 1L, Amount.of(1000)));

            BalanceCommand.PointUseCommand command = new BalanceCommand.PointUseCommand(1L, 1L,1000);

            //when
            balanceService.use(command);

            //then
            verify(balanceRepository, times(1)).savePointHistory(PointHistory.ofUse(1L, 1L, 1000));
        }

        @Test
        void 포인트_사용에_실패하면_포인트_내역_저장_레포지토리를_0회_호출() {

            //given
            when(balanceRepository.findByUserId(1L))
                    .thenReturn(Balance.of(1L, 1L, Amount.of(1000)));

            BalanceCommand.PointUseCommand command = new BalanceCommand.PointUseCommand(1L, 1L, 2000);

            //when, then
            assertThatThrownBy(() -> balanceService.use(command))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(balanceRepository, times(0)).savePointHistory(any());
        }

        @Test
        void 포인트_잔액에_사용_금액_뺀_포인트_반환() {

            //given
            when(balanceRepository.findByUserId(1L))
                    .thenReturn(Balance.of(1L, 1L, Amount.of(1000)));

            BalanceCommand.PointUseCommand command = new BalanceCommand.PointUseCommand(1L, 1L, 1000);

            //when
            Balance result = balanceService.use(command);

            //then
            assertThat(result).isEqualTo(Balance.of(1L, 1L, Amount.of(0)));
        }
    }
}