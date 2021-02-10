package no.ehfsok.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ehfsok.type.TaskStatus;

@Entity
@Builder @Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class TaskState {

	@Id
	private String id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TaskStatus status;

	private LocalDateTime lastRun;

	private LocalDateTime nextRun;

	@Column(nullable = false)
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;

}