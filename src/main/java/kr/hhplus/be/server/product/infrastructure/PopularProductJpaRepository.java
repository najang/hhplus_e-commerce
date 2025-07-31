package kr.hhplus.be.server.product.infrastructure;

import kr.hhplus.be.server.product.domain.entity.PopularProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PopularProductJpaRepository extends JpaRepository<PopularProduct, Long> {
    List<PopularProduct> findByOrderDateBetween(LocalDate orderDateAfter, LocalDate orderDateBefore);
}
