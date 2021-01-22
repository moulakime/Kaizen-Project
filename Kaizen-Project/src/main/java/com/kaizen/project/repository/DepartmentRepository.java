package com.kaizen.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kaizen.project.model.Department;

@Repository("departmentRepository")
public interface DepartmentRepository extends JpaRepository<Department , Integer> {


	
}
