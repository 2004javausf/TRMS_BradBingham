package com.revature.dao;

import java.sql.SQLException;
import java.util.List;

import com.revature.beans.Employee;

public interface EmployeeDAO {
//	public void insertEmployee(Employee e) throws SQLException;

	public List<Employee> getEmployeeList() throws SQLException;
	
	public Employee getEmployeeByName(String filter) throws SQLException;
	
	public void resetAvailableAmountAll() throws SQLException;
}
