package no.ehfsok.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.ehfsok.dao.CompanyRepo;
import no.ehfsok.dao.OrganizationFormRepo;
import no.ehfsok.model.Company;

@Service
public class CompanyService {

	// https://data.brreg.no/enhetsregisteret/api/enheter?organisasjonsnummer=925817139

	@Autowired
	private CompanyRepo companyRepo;

	@Autowired
	private OrganizationFormRepo organizationFormRepo;

	public Company getOrCreate(String name, int orgNumber) {
		return companyRepo.findById(orgNumber).orElseGet(() -> {

			Company company = new Company();
			company.setName(name);
			company.setOrganizationNumber(orgNumber);

			return companyRepo.save(company);
		});
	}
}