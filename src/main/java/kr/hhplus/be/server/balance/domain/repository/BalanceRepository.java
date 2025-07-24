package kr.hhplus.be.server.balance.domain.repository;

import kr.hhplus.be.server.balance.domain.entity.Balance;
import kr.hhplus.be.server.balance.domain.entity.PointHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository {
    Balance findByUserId(Long userId);

    PointHistory savePointHistory(PointHistory pointHistory);
}
