package org.babicz.springlab4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringLab4Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringLab4Application.class, args);
	}

}
