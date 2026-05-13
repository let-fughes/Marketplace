package com.kirylliuss.shop.authService;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AuthServiceApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		dotenv.entries().forEach(dotenvEntry ->
				System.setProperty(dotenvEntry.getKey(), dotenvEntry.getValue())
		);

		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
