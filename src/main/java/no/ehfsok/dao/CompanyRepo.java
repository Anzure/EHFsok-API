package no.ehfsok.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import no.ehfsok.model.Company;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {

	@Query(value = "select * from company where name like concat('%', :name, '%') order by name like concat(:name, '%') desc, ifnull(nullif(instr(name, concat(' ', :name)), 0), 99999), ifnull(nullif(instr(name, :name), 0), 99999), name LIMIT 36;", nativeQuery = true)
	List<Company> findBySearch(String name);

}