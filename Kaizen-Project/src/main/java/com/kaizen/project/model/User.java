package com.kaizen.project.model;

import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.persistence.JoinColumn;

@Entity
@Table(name="users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String first_name;
	private String last_name;
	@Column(unique=true)
	private int immatricule;
	private String job_title;
	private String company_name;
	private String town;
	private String country;
	@Email
	private String supervisor;
	private String department;
	@Column(unique=true)
	@Email
	private String email;
	@NotNull
	@Size(min = 4)
	private String password;
	private Date created_at;
	private Date updated_at;
	private String avatar;
	private Date deleted_at;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    				inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;
	/* Getters & Setters */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public int getImmatricule() {
		return immatricule;
	}
	public void setImmatricule(int immatricule) {
		this.immatricule = immatricule;
	}
	public String getJob_title() {
		return job_title;
	}
	public void setJob_title(String job_title) {
		this.job_title = job_title;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	public String getAvatar() {
		return avatar;
	}
	public Collection<Role> getRoles() {
		return roles;
	}
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public Date getDeleted_at() {
		return deleted_at;
	}
	public void setDeleted_at(Date deleted_at) {
		this.deleted_at = deleted_at;
	}

	public User() {
		
	}
	public User(String first_name, String last_name, int immatricule, String job_title, String company_name,
			String town, String country, @Email String supervisor, String department, @Email String email,
			@NotNull @Size(min = 4) String password, Date created_at, Date updated_at, String avatar, Date deleted_at) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.immatricule = immatricule;
		this.job_title = job_title;
		this.company_name = company_name;
		this.town = town;
		this.country = country;
		this.supervisor = supervisor;
		this.department = department;
		this.email = email;
		this.password = password;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.avatar = avatar;
		this.deleted_at = deleted_at;
	}
	public User(String first_name, String last_name, int immatricule, String job_title, String company_name,
			String town, String country, @Email String supervisor, String department, @Email String email,
			@NotNull @Size(min = 4) String password, Date created_at, Date updated_at, String avatar, Date deleted_at,
			Collection<com.kaizen.project.model.Role> roles) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.immatricule = immatricule;
		this.job_title = job_title;
		this.company_name = company_name;
		this.town = town;
		this.country = country;
		this.supervisor = supervisor;
		this.department = department;
		this.email = email;
		this.password = password;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.avatar = avatar;
		this.deleted_at = deleted_at;
		this.roles = roles;
	}


	
}
