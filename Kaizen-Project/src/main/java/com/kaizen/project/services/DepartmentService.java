package com.kaizen.project.services;


import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kaizen.project.model.Department;
import com.kaizen.project.repository.DepartmentRepository;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;
	

	public List<Department> getAllDepartments()
	{
		List<Department> departments = new ArrayList<Department>();
		for(Department department:departmentRepository.findAll())
		{
			departments.add(department);
		}
		return departments;
	}
	
	public Long getAllDepartmentsCount()
	{
		return departmentRepository.count();
	}
	
}
