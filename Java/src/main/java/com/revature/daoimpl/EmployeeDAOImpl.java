package com.revature.daoimpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Employee;
import com.revature.dao.EmployeeDAO;
import com.revature.util.ConnFactory;

public class EmployeeDAOImpl implements EmployeeDAO {
	public static ConnFactory cf = ConnFactory.getInstance();
//	@Override
//	public void insertEmployee(Employee e) throws SQLException {
//		String sql = "{ call INSERT_EMPLOYEE(?,?,?,?,?,?,?,?)";
//		Connection conn = cf.getConnection();
//		CallableStatement call = conn.prepareCall(sql);
//		call.setString(1, e.getFirstName());
//		call.setString(2, e.getLastName());
//		call.setString(3, e.getUsername());
//		call.setString(4, e.getPassword());
//		call.setDouble(5, e.getAvailableAmount());
//		call.setString(6, e.getTitle());
//		call.setString(7, e.getDepartment());
//		call.setString(8, e.getOfficeLoc());
//		call.execute();
//		call.close();
//	}

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
					rs.getString(4),
					rs.getString(5),
					rs.getDouble(6),
					rs.getString(7),
					rs.getString(8),
					rs.getString(9)
					);
			eList.add(e);
		}
		conn.close();
		return eList;
	}

	public Employee getEmployeeByName(String filter) {
		String sql = "SELECT * FROM EMPLOYEES WHERE USERNAME ='"+filter+"'";
		Connection conn = cf.getConnection();
		Employee e = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				e = new Employee(
						rs.getInt(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getDouble(6),
						rs.getString(7),
						rs.getString(8),
						rs.getString(9)
						);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return e;
	}
	public Employee getEmployeeById(String filter) {
		String sql = "SELECT * FROM EMPLOYEES WHERE ID ='"+filter+"'";
		Connection conn = cf.getConnection();
		Employee e = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				e = new Employee(
						rs.getInt(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getDouble(6),
						rs.getString(7),
						rs.getString(8),
						rs.getString(9)
						);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return e;
	}

	@Override
	public void resetAvailableAmountAll() throws SQLException {
		String sql = "UPDATE EMPLOYEES SET AVAILABLE_AMOUNT = 1000";
		Connection conn = cf.getConnection();
		Statement stmt = conn.createStatement();
		stmt.executeQuery(sql);
		conn.close();
		
	}

}
