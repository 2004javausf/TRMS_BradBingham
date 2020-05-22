package com.revature.daoimpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Employee;
import com.revature.beans.RForm;
import com.revature.dao.EmployeeDAO;
import com.revature.util.ConnFactory;

public class EmployeeDAOImpl implements EmployeeDAO {
	public static ConnFactory cf = ConnFactory.getInstance();
	@Override
	public void insertEmployee(Employee e) throws SQLException {
		String sql = "{ call INSERT_EMPLOYEE(?,?,?,?,?,?)";
		Connection conn = cf.getConnection();
		CallableStatement call = conn.prepareCall(sql);
		call.setString(1, e.getFirstName());
		call.setString(2, e.getLastName());
		call.setDouble(3, e.getAvailableAmount());
		call.setString(4, e.getTitle());
		call.setString(5, e.getDepartment());
		call.setString(6, e.getOfficeLoc());
		call.execute();
		call.close();
	}

	@Override
	public List<Employee> getEmployeeList() throws SQLException {
		String sql = "SELECT * FROM EMPLOYEES";
		Connection conn = cf.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		Employee e = null;
		ArrayList<Employee> eList = new ArrayList<>();
		while (rs.next()) {
			e = new Employee(
					rs.getInt(1),
					rs.getString(2),
					rs.getString(3),
					rs.getDouble(4),
					rs.getString(5),
					rs.getString(6),
					rs.getString(7)
					);
			eList.add(e);
		}
		return eList;
	}

}
