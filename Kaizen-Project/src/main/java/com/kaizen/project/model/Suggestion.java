package com.kaizen.project.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.JoinColumn;

@Entity
@Table(name="suggestions")
public class Suggestion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@TableGenerator(name = "Suggestion_Code",initialValue = 2000, allocationSize = 100)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "Suggestion_Code")
	private Long id;
	private String receipent_user;
	private String facilitator;
	private String problem_amelioration;
	private String proposed_solution;
	private String adopted_solution;
	private String choices;
	private String justification;
	private String comment;
	private String attachement_file;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date created_at;
	private Date updated_at;
	private Date deleted_at;
	@ManyToOne
	@JoinColumn(name="user_id")
    @NotNull(message = "Please fill user id")
	private User id_user;
	@OneToOne
	@JoinColumn(name="type_id")
    @NotNull(message = "Please fill type id")
	private Type id_type;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReceipent_user() {
		return receipent_user;
	}
	public void setReceipent_user(String receipent_user) {
		this.receipent_user = receipent_user;
	}
	public String getFacilitator() {
		return facilitator;
	}
	public void setFacilitator(String facilitator) {
		this.facilitator = facilitator;
	}
	public String getProblem_amelioration() {
		return problem_amelioration;
	}
	public void setProblem_amelioration(String problem_amelioration) {
		this.problem_amelioration = problem_amelioration;
	}
	public String getProposed_solution() {
		return proposed_solution;
	}
	public void setProposed_solution(String proposed_solution) {
		this.proposed_solution = proposed_solution;
	}
	public String getAdopted_solution() {
		return adopted_solution;
	}
	public void setAdopted_solution(String adopted_solution) {
		this.adopted_solution = adopted_solution;
	}
	public String getChoices() {
		return choices;
	}
	public void setChoices(String choices) {
		this.choices = choices;
	}
	public String getJustification() {
		return justification;
	}
	public void setJustification(String justification) {
		this.justification = justification;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getAttachement_file() {
		return attachement_file;
	}
	public void setAttachement_file(String attachement_file) {
		this.attachement_file = attachement_file;
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
	public Date getDeleted_at() {
		return deleted_at;
	}
	public void setDeleted_at(Date deleted_at) {
		this.deleted_at = deleted_at;
	}
	public User getUser_id() {
		return id_user;
	}
	public void setUser_id(User id_user) {
		this.id_user = id_user;
	}
	public Type getType_id() {
		return id_type;
	}
	public void setType_id(Type id_type) {
		this.id_type = id_type;
	}
	public Suggestion(String receipent_user, String facilitator, String problem_amelioration,
			String proposed_solution, String adopted_solution, String choices, String justification, String comment,
			String attachement_file, Date created_at, Date updated_at, Date deleted_at, User id_user,
			Type id_type) {
		super();
		this.receipent_user = receipent_user;
		this.facilitator = facilitator;
		this.problem_amelioration = problem_amelioration;
		this.proposed_solution = proposed_solution;
		this.adopted_solution = adopted_solution;
		this.choices = choices;
		this.justification = justification;
		this.comment = comment;
		this.attachement_file = attachement_file;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.id_user = id_user;
		this.id_type = id_type;
	}
	public Suggestion() {
		super();
	}
	public Suggestion(Long id, String receipent_user, String facilitator, String problem_amelioration,
			String proposed_solution, String adopted_solution, String choices, String justification, String comment,
			String attachement_file, Date created_at, Date updated_at, Date deleted_at,User id_user,
			Type id_type) {
		super();
		this.id = id;
		this.receipent_user = receipent_user;
		this.facilitator = facilitator;
		this.problem_amelioration = problem_amelioration;
		this.proposed_solution = proposed_solution;
		this.adopted_solution = adopted_solution;
		this.choices = choices;
		this.justification = justification;
		this.comment = comment;
		this.attachement_file = attachement_file;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.id_user = id_user;
		this.id_type = id_type;
	}
	
	

	
}
