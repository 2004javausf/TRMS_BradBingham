package com.revature.dao;

import java.sql.SQLException;
import java.util.List;

import com.revature.beans.Employee;
import com.revature.beans.RForm;
import com.revature.beans.StatusChange;

public interface RFormDAO {

	public void insertForm(RForm rf) throws SQLException;
	
	public List<RForm> getFormList() throws SQLException;
	
	public List<RForm> getFormsById(int id) throws SQLException;
	
	public void updateStatus(StatusChange sc) throws SQLException;
	
	public List<RForm> getManagerEmployees(Employee em) throws SQLException;
}
