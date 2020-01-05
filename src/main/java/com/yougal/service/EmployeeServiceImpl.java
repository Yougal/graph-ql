package com.yougal.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.yougal.entity.Department;
import com.yougal.entity.Employee;
import com.yougal.entity.JobHistory;
import com.yougal.entity.Location;
import com.yougal.repository.EmployeeRepository;
import com.yougal.repository.LocationRepository;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.errors.SchemaProblem;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	private GraphQLSchema graphQLSchema;
	
	@PersistenceContext
	EntityManager entityManager;
	
	
	public EmployeeServiceImpl()  throws SchemaProblem, IOException  {
		ClassPathResource classPathResource = new ClassPathResource("graphQL/schema/employee.graphql");
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(classPathResource.getFile());
        DataFetcher<Employee> employeeFetcherByName = new DataFetcher<Employee>() {
            @Override
            public Employee get(DataFetchingEnvironment environment) {
                String name = (String)environment.getArguments().get("name");
                if (name != null) {
                	return employeeRepository.findByfirstName(name);
                }
                return null;
            }
        };
        DataFetcher<List<Location>> locationFetcherByCity = new DataFetcher<List<Location>>() {
            @Override
            public List<Location> get(DataFetchingEnvironment environment) {
                String name = (String)environment.getArguments().get("city");
                if (name != null) {
                	return locationRepository.findByCity(name);
                }
                return null;
            }
        };
        
        DataFetcher<List<Employee>> employeeFetcherByTitle = new DataFetcher<List<Employee>>() {
            @Override
            public List<Employee> get(DataFetchingEnvironment environment) {
                String name = (String)environment.getVariables().get("title");
                if (name != null) {
                	return employeeRepository.findByTitle(name);
                }
                return null;
            }
        };
    
	 RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring().type("Query", 
			 												builder->builder.dataFetcher("employee", employeeFetcherByName)
			 												.dataFetcher("location", locationFetcherByCity)
			 												.dataFetcher("employeeByTitle", employeeFetcherByTitle)
			 											  )
			 			.type("EmployeeType",  builder->builder.dataFetcher("jobHistories", (DataFetchingEnvironment environment)->{
						                			return ((Employee)environment.getSource()).getJobHistories();
						 						}))
			 			.type("JobHistoryType",  
			 							builder->builder.dataFetcher("department",(DataFetchingEnvironment environment)->((JobHistory)environment.getSource()).getDepartment())
			 											.dataFetcher("job", (DataFetchingEnvironment environment)->((JobHistory)environment.getSource()).getJob()))
			 			.type("DepartmentType",builder->builder.dataFetcher("location",(DataFetchingEnvironment environment)->((Department)environment.getSource()).getLocation()))
			 			.type("LocationType",builder->builder.dataFetcher("employees",(DataFetchingEnvironment environment)->((Location)environment.getSource()).getDepartments()
			 					.stream().map(f->f.getEmployees()).flatMap(f->f.stream()).collect(Collectors.toList())))
                	.build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
	}
	
	@Transactional
	public ExecutionResult executeQuery(HashMap<String, Object> variables, String query){
		entityManager.createNativeQuery("select 1 from dual");
	    GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
		return build.execute(ExecutionInput.newExecutionInput()
											.query(query)
											.variables(variables));
	}

}
