package com.yougal.repository;

import org.springframework.data.repository.CrudRepository;

import com.yougal.entity.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long>{

	Employee findByfirstName(String name);
	
}
