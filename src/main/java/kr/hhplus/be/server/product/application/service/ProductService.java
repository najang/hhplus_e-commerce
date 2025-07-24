package kr.hhplus.be.server.product.application.service;

import kr.hhplus.be.server.order.domain.entity.OrderProduct;
import kr.hhplus.be.server.product.application.command.ProductCommand;
import kr.hhplus.be.server.product.domain.entity.PopularProduct;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("상품을 찾을 수 없습니다.");
        });
    }

    @Transactional(readOnly = true)
    public List<PopularProduct> findPopularProducts() {
        return Optional.ofNullable(productRepository.findPopularProducts())
                .orElse(List.of());
    }

    @Transactional
    public List<Product> decreaseStocks(List<ProductCommand.StockDecreaseCommand> commands) {

        List<Long> productIds = commands.stream()
                .map(ProductCommand.StockDecreaseCommand::productId)
                .toList();

        List<Product> products = productRepository.findByIdIn(productIds);

        Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));

        for (ProductCommand.StockDecreaseCommand command : commands) {

            Product product = productMap.get(command.productId());
            product.decreaseStock(command.count());
        }

        return products;
    }

    @Transactional
    public List<PopularProduct> createPopularProductStatistics(List<OrderProduct> orderProducts) {

        List<PopularProduct> popularProducts = orderProducts.stream()
                .map(PopularProduct::of)
                .toList();

        return productRepository.savePopularProducts(popularProducts);
    }
}