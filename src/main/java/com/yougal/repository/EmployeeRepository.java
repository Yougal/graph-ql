package com.yougal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.yougal.entity.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long>{

	Employee findByfirstName(String name);
	
	
	@Query("from Employee e where e.job.jobTitle= :jobTitle")
	List<Employee> findByTitle(@Param("jobTitle") String jobTitle);
	
	
}
