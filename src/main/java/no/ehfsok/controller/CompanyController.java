package no.ehfsok.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

	@GetMapping("/search")
	public List<Company> getSearchResult(String query) {
		
		List<Company> companies = companyRepo.findBySearch(query);
		
		return companies;
	}
	
}