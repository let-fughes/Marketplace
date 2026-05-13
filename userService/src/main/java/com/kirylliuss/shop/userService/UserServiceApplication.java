package com.kirylliuss.shop.userService;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UserServiceApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		dotenv.entries().forEach(dotenvEntry ->
				System.setProperty(dotenvEntry.getKey(), dotenvEntry.getValue())
		);

		SpringApplication.run(UserServiceApplication.class, args);
	}
}
