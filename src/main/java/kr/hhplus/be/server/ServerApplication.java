package kr.hhplus.be.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
		"kr.hhplus.be.server.balance.infrastructure",
		"kr.hhplus.be.server.coupon.infrastructure",
		"kr.hhplus.be.server.order.infrastructure",
		"kr.hhplus.be.server.product.infrastructure"
})
@EntityScan(basePackages = {
		"kr.hhplus.be.server.balance.domain.entity",
		"kr.hhplus.be.server.coupon.domain.entity",
		"kr.hhplus.be.server.order.domain.entity",
		"kr.hhplus.be.server.product.domain.entity"
})
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
