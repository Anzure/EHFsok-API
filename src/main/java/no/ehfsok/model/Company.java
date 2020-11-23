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

@Entity
public class Company {

	@Id
	private long organizationNumber;

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

	public long getOrganizationNumber() {
		return organizationNumber;
	}

	public void setOrganizationNumber(long organizationNumber) {
		this.organizationNumber = organizationNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OrganizationForm getOrganizationForm() {
		return organizationForm;
	}

	public void setOrganizationForm(OrganizationForm organizationForm) {
		this.organizationForm = organizationForm;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}