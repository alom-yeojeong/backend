package com.arom.yeojung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class YeojungApplication {

	public static void main(String[] args) {
		SpringApplication.run(YeojungApplication.class, args);
	}

}
