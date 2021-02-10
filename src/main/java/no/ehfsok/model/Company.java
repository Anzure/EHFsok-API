package no.ehfsok.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Company {

	@Id
	private Long organizationNumber;

	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name="organization_form_id", nullable=true)
	private OrganizationForm organizationForm;

	@Column(nullable = true)
	private LocalDate registrationDate;

	@OneToOne(mappedBy = "company")
	private ElmaDetail elmaDetail;

	@Column(nullable = false)
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;

}