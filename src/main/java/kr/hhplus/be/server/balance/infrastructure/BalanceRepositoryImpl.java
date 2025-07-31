package kr.hhplus.be.server.balance.infrastructure;

import kr.hhplus.be.server.balance.domain.entity.Balance;
import kr.hhplus.be.server.balance.domain.entity.PointHistory;
import kr.hhplus.be.server.balance.domain.repository.BalanceRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BalanceRepositoryImpl implements BalanceRepository {
    private final BalanceJpaRepository balanceJpaRepository;
    private final PointHistoryJpaRepository pointHistoryJpaRepository;

    public BalanceRepositoryImpl(BalanceJpaRepository balanceJpaRepository, PointHistoryJpaRepository pointHistoryJpaRepository) {
        this.balanceJpaRepository = balanceJpaRepository;
        this.pointHistoryJpaRepository = pointHistoryJpaRepository;
    }

    @Override
    public Balance findByUserId(Long userId) {
        return balanceJpaRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public PointHistory savePointHistory(PointHistory pointHistory) {
        return pointHistoryJpaRepository.save(pointHistory);
    }
}
