package com.revature.dao;

import java.sql.SQLException;
import java.util.List;

import com.revature.beans.RForm;

public interface RFormDAO {

	public void insertForm(RForm rf) throws SQLException;
	
	public List<RForm> getFormList() throws SQLException;
	
}
