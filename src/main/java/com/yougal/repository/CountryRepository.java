package com.yougal.repository;

import org.springframework.data.repository.CrudRepository;

import com.yougal.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Long>{

	Country findByCountryNameIgnoreCase(String countryName);
	
}
