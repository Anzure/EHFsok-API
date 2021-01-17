package no.ehfsok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories
@ServletComponentScan
public class EhfSokApplication {

	public static void main(String[] args) {
		SpringApplication.run(EhfSokApplication.class, args);
	}

}