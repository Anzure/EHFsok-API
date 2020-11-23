package no.ehfsok.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import no.ehfsok.model.Company;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {

}