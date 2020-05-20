package com.revature.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.revature.beans.RForm;
import com.revature.dao.RFormDAO;
import com.revature.util.ConnFactory;

public class RFormDAOImpl implements RFormDAO {
	public static ConnFactory cf = ConnFactory.getInstance();

	@Override
	public void insertForm(RForm rf) throws SQLException {
		String sql = "INSERT INTO REIMBURSE_FORM VALUES(?,?)";
		Connection conn = cf.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, rf.getId());
		ps.setString(2, rf.getContents());
		ps.executeUpdate();
	}

	@Override
	public ArrayList<RForm> getFormList() throws SQLException {
		String sql = "SELECT * FROM REIMBURSE_FORM";
		Connection conn = cf.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		RForm rf = null;
		ArrayList<RForm> rfList = new ArrayList<>();
		while (rs.next()) {
			rf = new RForm(rs.getInt(1), rs.getString(2));
			rfList.add(rf);
		}
		return rfList;
	}

}
