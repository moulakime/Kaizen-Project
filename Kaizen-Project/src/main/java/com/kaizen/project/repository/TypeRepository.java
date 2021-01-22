package com.kaizen.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kaizen.project.model.Type;

@Repository("typeRepository")
public interface TypeRepository extends JpaRepository<Type , Integer> {


	
}
