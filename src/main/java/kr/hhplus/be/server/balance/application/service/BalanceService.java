package kr.hhplus.be.server.balance.application.service;

import kr.hhplus.be.server.balance.application.command.BalanceCommand;
import kr.hhplus.be.server.balance.domain.entity.Balance;
import kr.hhplus.be.server.balance.domain.entity.PointHistory;
import kr.hhplus.be.server.balance.domain.repository.BalanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @Transactional(readOnly = true)
    public Balance findByUserId(long userId) {
        return balanceRepository.findByUserId(userId);
    }

    @Transactional
    public Balance charge(BalanceCommand.BalanceChargeCommand command) {

        Balance balance = findByUserId(command.userId());
        balance.charge(command.amount());

        PointHistory pointHistory = PointHistory.ofCharge(balance.getId(), command.amount());
        balanceRepository.savePointHistory(pointHistory);

        return balance;
    }

    @Transactional
    public Balance use(BalanceCommand.PointUseCommand command) {

        Balance point = findByUserId(command.userId());
        point.use(command.amount());

        PointHistory pointHistory = PointHistory.ofUse(point.getId(), command.orderId(), command.amount());
        balanceRepository.savePointHistory(pointHistory);

        return point;
    }
}