package io.spring.riffdemo;

import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RiffDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiffDemoApplication.class, args);
	}

	@Bean
	public Function<String, String> reverse() {
		return s -> new StringBuilder(s).reverse().toString();
	}
}
