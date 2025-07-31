package kr.hhplus.be.server.product.infrastructure;

import kr.hhplus.be.server.product.domain.entity.PopularProduct;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final PopularProductJpaRepository popularProductJpaRepository;

    public ProductRepositoryImpl(ProductJpaRepository productJpaRepository, PopularProductJpaRepository popularProductJpaRepository) {
        this.productJpaRepository = productJpaRepository;
        this.popularProductJpaRepository = popularProductJpaRepository;
    }

    @Override
    public Optional<Product> findById(long id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public List<PopularProduct> findPopularProducts() {
        return popularProductJpaRepository.findByOrderDateBetween(LocalDate.now().minusDays(3), LocalDate.now().minusDays(1));
    }

    @Override
    public List<Product> findByIdIn(List<Long> ids) {
        return productJpaRepository.findAllById(ids);
    }

    @Override
    public List<PopularProduct> savePopularProducts(List<PopularProduct> popularItems) {
        return popularProductJpaRepository.saveAll(popularItems);
    }
}
