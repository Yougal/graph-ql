package com.yougal.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.yougal.entity.Country;
import com.yougal.entity.Employee;
import com.yougal.entity.Location;
import com.yougal.repository.EmployeeRepository;
import com.yougal.service.EmployeeService;

import graphql.ExecutionResult;
import graphql.schema.idl.errors.SchemaProblem;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {EmployeeRepository.class})
@EntityScan(basePackageClasses = {Employee.class})
@ComponentScan(basePackageClasses = {EmployeeService.class})
@EnableAutoConfiguration(exclude = {WebMvcAutoConfiguration.class})
public class StartApplication {
	
	

	public static void main(String[] args) throws SchemaProblem, IOException {
		ConfigurableApplicationContext context = SpringApplication.run(StartApplication.class, args);
		EmployeeService employeeService = context.getBean(EmployeeService.class);
        
        HashMap<String, Object> variables = new HashMap<>();
        ClassPathResource classPathResource = null;
        String query = null;
        
       /** 
        variables.put("title", "Programmer");
        classPathResource = new ClassPathResource("graphQL/query/employee-by-name-query.graphql");
		query = new String(Files.readAllBytes(Paths.get(classPathResource.getURI())));
		executeQuery(employeeService, variables, query);
		
		classPathResource = new ClassPathResource("graphQL/query/employee-by-location.graphql");
		query = new String(Files.readAllBytes(Paths.get(classPathResource.getURI())));
		executeQuery(employeeService, variables, query);
		
		classPathResource = new ClassPathResource("graphQL/query/employee-by-title.graphql");
		query = new String(Files.readAllBytes(Paths.get(classPathResource.getURI())));
		executeQuery(employeeService, variables, query);**/
		
		
		classPathResource = new ClassPathResource("graphQL/mutation/location-mutation.graphql");
		query = new String(Files.readAllBytes(Paths.get(classPathResource.getURI())));
		variables.clear();
		HashMap<String, String> object = new HashMap<>();
		object.put("city","DELHI");
		object.put("postalCode","113001");
		object.put("stateProvince","DELHI");
		object.put("streetAddress","140, BLUE TOWER");
		object.put("countryName","INDIA");
		variables.put("location", object);
		executeMutation(employeeService, variables, query);
	}

	private static void executeQuery(EmployeeService employeeService, HashMap<String, Object> variables, String query) {
		ExecutionResult executionResult = employeeService.executeQuery(variables, query);
		
		if(executionResult.getErrors().size()>0) {
			System.out.println(executionResult.getErrors());
		}else {
			DumperOptions options = new DumperOptions();
	        options.setIndent(2);
	        options.setPrettyFlow(true);
	        // Fix below - additional configuration
	        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
	        Yaml output = new Yaml(options);
			System.out.println(output.dump(executionResult.getData()));
		}
	}
	private static void executeMutation(EmployeeService employeeService, HashMap<String, Object> variables, String query) {
		ExecutionResult executionResult = employeeService.executeQuery(variables, query);
		
		if(executionResult.getErrors().size()>0) {
			System.out.println(executionResult.getErrors());
		}else {
			DumperOptions options = new DumperOptions();
	        options.setIndent(2);
	        options.setPrettyFlow(true);
	        // Fix below - additional configuration
	        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
	        Yaml output = new Yaml(options);
			System.out.println(output.dump(executionResult.getData()));
		}
	}
}
