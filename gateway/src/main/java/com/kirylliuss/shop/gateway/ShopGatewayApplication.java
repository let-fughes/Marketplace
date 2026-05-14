package com.kirylliuss.shop.gateway;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.kirylliuss.shop.gateway")
public class ShopGatewayApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		dotenv.entries().forEach(dotenvEntry ->
				System.setProperty(dotenvEntry.getKey(), dotenvEntry.getValue()));

		SpringApplication.run(ShopGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customGatewayRoutes(RouteLocatorBuilder builder){
		return builder.routes()
				.route("user-service-route", r -> r
						.path("/v1/api/users/**")
						.uri("http://localhost:8081"))
				.route("auth-service", r -> r
						.path("/v1/api/auth/**")
						.uri("http://localhost:8082"))
				.route("order-service-1", r -> r
						.path("/v1/api/orders/**")
						.uri("http://localhost:8083"))
				.route("order-service-2", r -> r
						.path("/v1/api/items/**")
						.uri("http://localhost:8083"))
				.route("payment-service", r -> r
						.path("/v1/api/payments/**")
						.uri("http://localhost:8084"))
				.build();
	}

}
