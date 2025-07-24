package kr.hhplus.be.server.product.facade;

import kr.hhplus.be.server.order.application.service.OrderService;
import kr.hhplus.be.server.order.domain.entity.OrderProduct;
import kr.hhplus.be.server.product.application.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductFacadeService {

    private final ProductService productService;
    private final OrderService orderService;

    public ProductFacadeService(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @Transactional
    public void createOrderProductStatistics() {

        List<OrderProduct> orderProducts = orderService.findTodayOrderProducts();

        productService.createPopularProductStatistics(orderProducts);
    }
}