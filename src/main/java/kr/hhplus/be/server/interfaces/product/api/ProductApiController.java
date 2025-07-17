package kr.hhplus.be.server.interfaces.product.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductApiController implements ProductApiSpec {

    @GetMapping("/{productId}")
    @Override
    public ResponseEntity<ProductResponse> product(Long productId) {
        return ResponseEntity.ok(
                ProductResponse.of(1L, "item1", new BigDecimal("30000"), new BigDecimal("100"))
        );
    }

    @GetMapping("/popular")
    @Override
    public ResponseEntity<List<ProductResponse>> getPopularProducts() {
        return ResponseEntity.ok(
                List.of(
                        ProductResponse.of(2L, "item2", new BigDecimal("30000"), new BigDecimal("100")),
                        ProductResponse.of(3L, "item3", new BigDecimal("50000"), new BigDecimal("40"))
                )
        );
    }

    @PostMapping("/popular")
    @Override
    public ResponseEntity<ProductResponse> setPopularProductsStatistics(ProductRequest request) {
        return ResponseEntity.ok(
                ProductResponse.of(1L, "item4", new BigDecimal("40000"), new BigDecimal("50"))
        );
    }
}
