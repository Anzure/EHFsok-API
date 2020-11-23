package no.ehfsok.dao;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.ehfsok.model.Company;
import no.ehfsok.model.ElmaDetail;

@Repository
public interface ElmaDetailRepo extends JpaRepository<ElmaDetail, UUID> {

	Optional<ElmaDetail> findByCompany(Company company);
	
}