package kr.hhplus.be.server.product.domain.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Stock stock;

    private int price;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Product of(Long id, String name, Stock stock, int price) {
        return new Product(id, name, stock, price, LocalDateTime.now(), LocalDateTime.now());
    }

    public Product(Long id, String name, Stock stock, int price, LocalDateTime createdAt, LocalDateTime updatedAt) {


        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("상품명을 입력해주세요.");
        }
        if (stock == null) {
            throw new IllegalArgumentException("재고 정보가 필요합니다.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }

        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void decreaseStock(int count) {
        this.stock = stock.decrease(count);
    }

    public void increaseStock(int count) {
        this.stock = stock.increase(count);
    }

    public int getStock() {
        return stock.getCount();
    }
}