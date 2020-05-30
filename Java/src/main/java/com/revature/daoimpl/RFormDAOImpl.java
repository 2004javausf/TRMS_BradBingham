package com.revature.daoimpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Employee;
import com.revature.beans.RForm;
import com.revature.beans.StatusChange;
import com.revature.dao.RFormDAO;
import com.revature.util.ConnFactory;

import oracle.sql.BLOB;

public class RFormDAOImpl implements RFormDAO {
	public static ConnFactory cf = ConnFactory.getInstance();

	@Override
	public void insertForm(RForm rf) throws SQLException {
		String sql = "{ call INSERT_R_FORM(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = cf.getConnection();
		CallableStatement call = conn.prepareCall(sql);
		call.setInt(1, rf.getEmpID());
		call.setString(2, formatDate(rf.getStartDate()));
		call.setString(3, rf.getStartTime());
		call.setString(4, rf.getLocation());
		call.setDouble(5, rf.getCost());
		call.setDouble(6, rf.getPendingRe());
		call.setString(7, rf.getDescription());
		call.setString(8, rf.getJustification());
		call.setInt(9, rf.getGradeFormatID());
		call.setString(10, rf.getEventType());
		call.setString(11, rf.getOnSubmit());
		call.setString(12, rf.getIsUrgent());
		call.setString(13, rf.getSupApr());
		call.setString(14, formatDate(rf.getSupSubDate()));
		call.setString(15, rf.getHeadApr());
		call.setString(16, formatDate(rf.getHeadSubDate()));
		
		call.execute();
		call.close();
	}

	private static String formatDate(String test) {
		StringBuilder sb = new StringBuilder();
		if(test == null)
			return null;
		sb.append(test.substring(8, 10));
		String key = test.substring(5, 7);
		switch (key) {
		case "01":
			sb.append("-JAN-");
			break;
		case "02":
			sb.append("-FEB-");
			break;
		case "03":
			sb.append("-MAR-");
			break;
		case "04":
			sb.append("-APR-");
			break;
		case "05":
			sb.append("-MAY-");
			break;
		case "06":
			sb.append("-JUN-");
			break;
		case "07":
			sb.append("-JUL-");
			break;
		case "08":
			sb.append("-AUG-");
			break;
		case "09":
			sb.append("-SEP-");
			break;
		case "10":
			sb.append("-OCT-");
			break;
		case "11":
			sb.append("-NOV-");
			break;
		case "12":
			sb.append("-DEC-");
			break;
		default:
			break;
		}
		sb.append(test.substring(0, 4));
			return sb.toString();			
		
	}
	private ArrayList<RForm> runQuery(String sql) throws SQLException{
		Connection conn = cf.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		RForm rf = null;
		ArrayList<RForm> rfList = new ArrayList<>();
		while (rs.next()) {
			rf = new RForm(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5),
					rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
					rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15),
					rs.getDouble(16), rs.getDouble(17), rs.getString(18), rs.getString(19), rs.getInt(20),
					rs.getString(21), rs.getString(22), rs.getDouble(23), rs.getString(24), rs.getString(25),
					rs.getString(26),rs.getString(27));
			rfList.add(rf);
		}
		return rfList;
	}

	@Override
	public ArrayList<RForm> getFormList() throws SQLException {
		String sql = "SELECT * FROM R_FORMS";
		return runQuery(sql);
	}

	@Override
	public List<RForm> getFormsById(int id) throws SQLException {
		String sql = "SELECT * FROM R_FORMS WHERE EMPLOYEE_ID =" + id;
		return runQuery(sql);
	}

	

	@Override
	public void updateStatus(StatusChange sc)
			throws SQLException {
		String sql = "{ call R_FORM_STATUS_CHANGE(?,?,?,?,?,?)";
		Connection conn = cf.getConnection();
		CallableStatement call = conn.prepareCall(sql);
		call.setInt(1, sc.getRfId());
		call.setInt(2, sc.getEmpId());
		call.setInt(3, sc.getAprId());
		call.setString(4, sc.getTitle());
		call.setString(5, sc.getNewStatus());
		call.setString(6, sc.getReason());
		call.execute();
		call.close();
	}

	public int getNextFormId() throws SQLException {
		String sql = "SELECT R_FORM_SEQ.NEXTVAL -1 FROM DUAL";
		int result = 0;
		Connection conn = cf.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		return rs.getInt(1);
	}
	
	public List<RForm> getManagerEmployees(Employee em) throws SQLException{
		String filter = "";
		String deptFilter = "AND DEPARTMENT = '"+em.getDepartment();
		switch (em.getTitle()) {
		case "Supervisor": filter = deptFilter+"' AND (r_forms.approve_supervisor = 'False' OR r_forms.form_status = 'Pending')" ;break;
		case "Head": filter = deptFilter+"' AND r_forms.approve_supervisor = 'Approved'"; break;
		case "Associate": 
			if(em.getDepartment().equals("Benefits")) {
				filter = "AND r_forms.approve_head = 'Approved'"; 
			} else filter = "AND r_forms.approve_head = 'Not a Manager'";break;
		default: break;
		}
		String sql = "SELECT R_FORMS.ID ID,\r\n" + 
				"R_FORMS.EMPLOYEE_ID EMPLOYEE_ID,\r\n" + 
				"R_FORMS.FORM_STATUS FORM_STATUS,\r\n" + 
				"R_FORMS.APPROVE_SUPERVISOR APPROVE_SUPERVISOR,\r\n" + 
				"R_FORMS.SUPERVISOR_SUBMIT_DATE SUPERVISOR_SUBMIT_DATE,\r\n" + 
				"R_FORMS.APPROVE_HEAD APPROVE_HEAD,\r\n" + 
				"R_FORMS.HEAD_SUBMIT_DATE HEAD_SUBMIT_DATE,\r\n" + 
				"R_FORMS.APPROVE_COORDINATOR APPROVE_COORDINATOR,\r\n" + 
				"R_FORMS.COORDINATOR_SUBMIT_DATE COORDINATOR_SUBMIT_DATE,\r\n" + 
				"R_FORMS.ALTERED_FORM ALTERED_FORM,\r\n" + 
				"R_FORMS.REJECTION_JUSTIFY REJECTION_JUSTIFY,\r\n" + 
				"R_FORMS.SUBMIT_DATE SUBMIT_DATE,\r\n" + 
				"R_FORMS.START_DATE START_DATE,\r\n" + 
				"R_FORMS.START_TIME START_TIME,\r\n" + 
				"R_FORMS.EVENT_LOCATION EVENT_LOCATION,\r\n" + 
				"R_FORMS.EVENT_COST EVENT_COST,\r\n" + 
				"R_FORMS.PENDING_REIMBURSEMENT PENDING_REIMBURSEMENT,\r\n" + 
				"R_FORMS.EVENT_DESCRIPTION EVENT_DESCRIPTION,\r\n" + 
				"R_FORMS.EVENT_JUSTIFY EVENT_JUSTIFY,\r\n" + 
				"R_FORMS.GRADING_FORMAT_ID GRADING_FORMAT_ID,\r\n" + 
				"R_FORMS.EVENT_TYPE EVENT_TYPE,\r\n" + 
				"R_FORMS.OPT_ON_SUBMIT OPT_ON_SUBMIT,\r\n" + 
				"R_FORMS.ON_FINISH_GRADE ON_FINISH_GRADE,\r\n" + 
				"R_FORMS.APPROVE_GRADE APPROVE_GRADE,\r\n" + 
				"R_FORMS.ON_FINISH_PRESENTATION ON_FINISH_PRESENTATION,\r\n" + 
				"R_FORMS.APPROVE_PRESENTATION APPROVE_PRESENTATION,\r\n" + 
				"R_FORMS.IS_URGENT IS_URGENT\r\n"+
				"FROM R_FORMS INNER JOIN EMPLOYEES ON (R_FORMS.ID = EMPLOYEES.ID) \r\n" + 
				"WHERE OFFICE_LOC = '"+em.getOfficeLoc()+"'"+filter;
		return runQuery(sql);
	}
}
