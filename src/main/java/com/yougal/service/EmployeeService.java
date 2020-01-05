package com.yougal.service;

import java.util.HashMap;

import graphql.ExecutionResult;

public interface EmployeeService {
	ExecutionResult executeQuery(HashMap<String, Object> variables, String query);
}
