package com.kaizen.project.model;

import javax.annotation.ManagedBean;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="departments")
public class Department {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String department_name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDepartment_name() {
		return department_name;
	}
	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}
	public Department(Integer id,String department_name) {
		this.id = id;
		this.department_name = department_name;
	}
	public Department() {
	}
	

}
