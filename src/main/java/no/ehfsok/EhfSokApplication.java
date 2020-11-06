package no.ehfsok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class EhfSokApplication {

	public static void main(String[] args) {
		SpringApplication.run(EhfSokApplication.class, args);
	}

}