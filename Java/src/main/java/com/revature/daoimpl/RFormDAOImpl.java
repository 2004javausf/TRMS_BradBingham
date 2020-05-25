package com.revature.daoimpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.RForm;
import com.revature.dao.RFormDAO;
import com.revature.util.ConnFactory;

import oracle.sql.BLOB;

public class RFormDAOImpl implements RFormDAO {
	public static ConnFactory cf = ConnFactory.getInstance();

	@Override
	public void insertForm(RForm rf) throws SQLException {
		String sql = "{ call INSERT_R_FORM(?,?,?,?,?,?,?,?,?,?)";
		Connection conn = cf.getConnection();
		CallableStatement call = conn.prepareCall(sql);
		call.setInt(1, rf.getEmpID());
		call.setString(2, formatDate(rf.getStartDate()));
		call.setString(3, rf.getStartTime());
		call.setString(4, rf.getLocation());
		call.setDouble(5, rf.getCost());
		call.setString(6, rf.getDescription());
		call.setString(7, rf.getJustification());
		call.setInt(8, rf.getGradeFormatID());
		call.setString(9, rf.getEventType());
		call.setString(10, rf.getOnSubmit());
		call.execute();
		call.close();
	}
	private static String formatDate(String test) {
		StringBuilder sb = new StringBuilder();
		sb.append(test.substring(8, 10)+"-");
		String key = test.substring(5,7);
		switch (key) {
		case "01": sb.append("JAN-"); break;
		case "02": sb.append("FEB-"); break;
		case "03": sb.append("MAR-"); break;
		case "04": sb.append("APR-"); break;
		case "05": sb.append("MAY-"); break;
		case "06": sb.append("JUN-"); break;
		case "07": sb.append("JUL-"); break;
		case "08": sb.append("AUG-"); break;
		case "09": sb.append("SEP-"); break;
		case "10": sb.append("OCT-"); break;
		case "11": sb.append("NOV-"); break;
		case "12": sb.append("DEC-"); break;
		default:break;
		}
		sb.append(test.substring(0, 4));
		return sb.toString();
	}
	@Override
	public ArrayList<RForm> getFormList() throws SQLException {
		String sql = "SELECT * FROM R_FORMS";
		Connection conn = cf.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		RForm rf = null;
		ArrayList<RForm> rfList = new ArrayList<>();
		while (rs.next()) {
			rf = new RForm(
					rs.getInt(1),
					rs.getInt(2),
					rs.getString(3),
					rs.getString(4),
					rs.getString(5),
					rs.getString(6),
					rs.getString(7),
					rs.getString(8),
					rs.getString(9),
					rs.getString(10),
					rs.getString(11),
					rs.getString(12),
					rs.getString(13),
					rs.getString(14),
					rs.getString(15),
					rs.getDouble(16),
					rs.getString(17),
					rs.getString(18),
					rs.getInt(19),
					rs.getString(20),
					rs.getString(21),
					rs.getDouble(22),
					rs.getString(23),
					rs.getString(24),
					rs.getString(25)
					);
			rfList.add(rf);
		}
		return rfList;
	}

	@Override
	public List<RForm> getFormsById(int id) throws SQLException {
		String sql = "SELECT * FROM R_FORMS WHERE EMPLOYEE_ID ="+id;
		Connection conn = cf.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		RForm rf = null;
		ArrayList<RForm> rfList = new ArrayList<>();
		while (rs.next()) {
			rf = new RForm(
					rs.getInt(1),
					rs.getInt(2),
					rs.getString(3),
					rs.getString(4),
					rs.getString(5),
					rs.getString(6),
					rs.getString(7),
					rs.getString(8),
					rs.getString(9),
					rs.getString(10),
					rs.getString(11),
					rs.getString(12),
					rs.getString(13),
					rs.getString(14),
					rs.getString(15),
					rs.getDouble(16),
					rs.getString(17),
					rs.getString(18),
					rs.getInt(19),
					rs.getString(20),
					rs.getString(21),
					rs.getDouble(22),
					rs.getString(23),
					rs.getString(24),
					rs.getString(25)
					);
			rfList.add(rf);
		}
		return rfList;
	}

}
