package org.babicz.springlab5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringLab5Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringLab5Application.class, args);
	}

}
