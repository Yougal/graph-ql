package com.yougal.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.yougal.entity.Location;

public interface LocationRepository extends CrudRepository<Location, Long>{

	List<Location> findByCity(String city);
	
}
