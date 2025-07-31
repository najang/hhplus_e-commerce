## 인기 상품 목록 조회

### 목적

- 인기 상품 목록 조회시 주문(Order)와 상품(Product) 등 여러 도메인의 데이터를 통합적으로 분석해야 하기 떄문에 가장 문제가 될 수 있는 기능이라고 생각했습니다.
- 정렬, 필터링, 집계 등 여러 연산이 함께 수행되기 때문에 단순 조회 쿼리들보다 성늘 부하가 클 것으로 예상했습니다.

###  요구사항

- 사용자는 최근 3일(당일로부터 1일 전 ~ 3일 전)간 가장 많이 팔린 상위 5개 상품 정보를 제공 받습니다.

### 테스트 환경

- 10만 건 이상의 더미 데이터를 기반으로 EXPLAIN ANALYZE 명령어를 통해 인덱스 적용 전후의 성능을 분석했습니다.

### JPA (통계 테이블에서 조회)

```java
PopularProductJpaRepository.findByOrderDateBetween(orderDateAfter, orderDateBefore);
```

### 인덱스 적용 전
```sql
EXPLAIN ANALYZE
SELECT * FROM popular_product
WHERE order_date BETWEEN '2024-01-01' AND '2024-01-03';
```

```text
-> Filter: (popular_product.order_date between '2024-01-01' and '2024-01-03')  (cost=10815 rows=11918) (actual time=1.06..63.4 rows=10641 loops=1)
    -> Table scan on popular_product  (cost=10815 rows=107272) (actual time=1.04..32.3 rows=107169 loops=1)

```

### 인덱스 생성

- order_date에 대한 범위 조회(range scan)가 일어나므로 단일 컬럼 인덱스로 지정했습니다.
```sql
CREATE INDEX idx_order_date ON popular_product(order_date);
```

### 인덱스 적용 후

```text
-> Index range scan on popular_product using idx_order_date over ('2024-01-01' <= order_date <= '2024-01-03'), with index condition: (popular_product.order_date between '2024-01-01' and '2024-01-03')  (cost=8741 rows=19424) (actual time=5.01..30.9 rows=10641 loops=1)
```

### 성능 개선 사항

- 63.4 -> 30.9 (약 51.26% 개선)

### 추후 계획

- 확장성 고려: 테이블 구조가 복잡해지고 연관관계가 늘어날 경우, 다음과 같은 추가 방안을 검토해야 합니다.

    - 테이블 파티셔닝
    - 데이터 캐싱 전략
    - 데이터베이스 뷰(view) 활용
    - 읽기/쓰기 분리 아키텍처 구성

- 성능 테스트: 인덱스 적용 전후 성능 테스트를 통해 개선 효과를 측정하고 문서화하는 것이 중요합니다.