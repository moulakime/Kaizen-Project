package com.kaizen.project.services;


import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.kaizen.project.model.Type;
import com.kaizen.project.repository.TypeRepository;

@Service
public class TypeService {

	@Autowired
	private TypeRepository typeRepository;
	

	public List<Type> getAllTypes()
	{
		List<Type> types = new ArrayList<Type>();
		for(Type type:typeRepository.findAll(new Sort(Sort.Direction.ASC,"id")))
		{
			types.add(type);
		}
		return types;
	}
	
	
}
