package no.ehfsok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories
@ServletComponentScan
@EnableScheduling
public class EhfSokApplication {

	public static void main(String[] args) {
		SpringApplication.run(EhfSokApplication.class, args);
	}

}