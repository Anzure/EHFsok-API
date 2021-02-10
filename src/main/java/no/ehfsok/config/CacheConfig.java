package no.ehfsok.config;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CacheConfig {

	@CacheEvict(allEntries = true, value = {"company-search", "company-details"})
	@Scheduled(fixedDelay = 30*60*1000, initialDelay = 15*60*1000)
	public void clearCache() {
		log.debug("Cache cleared.");
	}

}