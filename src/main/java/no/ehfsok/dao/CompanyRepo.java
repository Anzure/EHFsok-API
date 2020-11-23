package no.ehfsok.dao;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import no.ehfsok.model.Company;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Integer> {

}