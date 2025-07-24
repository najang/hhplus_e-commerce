package kr.hhplus.be.server.product.domain.repository;

import kr.hhplus.be.server.product.domain.entity.PopularProduct;
import kr.hhplus.be.server.product.domain.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository {
    Optional<Product> findById(long id);

    List<PopularProduct> findPopularProducts();

    List<Product> findByIdIn(List<Long> ids);

    List<PopularProduct> savePopularProducts(List<PopularProduct> popularItems);
}