package no.ehfsok.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class ElmaDetail {

	@Id
	@GeneratedValue
	@Type(type="uuid-char")
	private UUID id;

	@OneToOne
	@JoinColumn(name="organization_number", nullable=true)
	private Company company;

	@Column(nullable = false)
	private Integer icd;

	@Column(nullable = false)
	private LocalDate registrationDate;

	@Column(nullable = false)
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;

}