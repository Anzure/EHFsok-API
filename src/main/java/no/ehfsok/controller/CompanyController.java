package no.ehfsok.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import no.ehfsok.dao.CompanyRepo;
import no.ehfsok.model.Company;

@RestController
@CrossOrigin
public class CompanyController {

	@Autowired
	private CompanyRepo companyRepo;

	@GetMapping({"/company/search", "/search"})
	@Cacheable("company-search")
	public ResponseEntity<List<Company>> getSearchResult(String query) {

		List<Company> companies = companyRepo.findBySearch(query);

		return ResponseEntity.ok(companies);
	}

	@GetMapping({"/company/details", "/company"})
	@Cacheable("company-details")
	public ResponseEntity<Company> getCompanyDetails(long organizationNumber) {

		Optional<Company> company = companyRepo.findById(organizationNumber);

		if (company.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		else {
			return ResponseEntity.ok(company.get());
		}
	}

}