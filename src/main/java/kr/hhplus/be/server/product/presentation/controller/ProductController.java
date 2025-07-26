package kr.hhplus.be.server.product.presentation.controller;

import kr.hhplus.be.server.product.application.dto.request.ProductRequest;
import kr.hhplus.be.server.product.application.dto.response.ProductResponse;
import kr.hhplus.be.server.product.application.service.ProductService;
import kr.hhplus.be.server.product.interfaces.api.ProductApiSpec;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController implements ProductApiSpec {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    @Override
    public ResponseEntity<ProductResponse.ProductDetailResponse> getProduct(Long productId) {
        ProductResponse.ProductDetailResponse response = ProductResponse.ProductDetailResponse.from(productService.findById(productId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/popular")
    @Override
    public ResponseEntity<List<ProductResponse.PopularProductDetailResponse>> getPopularProducts() {
        List<ProductResponse.PopularProductDetailResponse> response = productService.findPopularProducts().stream()
                .map(ProductResponse.PopularProductDetailResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }
}
