package kr.hhplus.be.server.interfaces.product.api;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ProductResponse {

    private Long productId;
    private String name;
    private BigDecimal price;
    private BigDecimal stock;

    @Builder
    private ProductResponse(Long productId, String name, BigDecimal price, BigDecimal stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public static ProductResponse of(Long productId, String name, BigDecimal price, BigDecimal stock) {
        return new ProductResponse(productId, name, price, stock);
    }
}