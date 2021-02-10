package no.ehfsok.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import no.ehfsok.model.TaskState;

@Repository
public interface TaskStateRepo extends JpaRepository<TaskState, String> {

}