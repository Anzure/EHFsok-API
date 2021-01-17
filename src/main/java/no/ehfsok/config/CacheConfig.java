package no.ehfsok.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class CacheConfig {

	private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

	@CacheEvict(allEntries = true, value = {"company-search", "company-details"})
	@Scheduled(fixedDelay = 30*60*1000, initialDelay = 15*60*1000)
	public void clearCache() {
		log.debug("Cache cleared.");
	}

}